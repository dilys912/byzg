package nc.ui.dm.dm104;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.bs.framework.common.NCLocator;
import nc.itf.scm.recordtime.IRecordTime;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.cardpanel.DMBillListPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.vo.dm.dm104.DelivConvertToOutHHeaderVO;
import nc.vo.dm.dm104.DelivConvertToOutHItemVO;
import nc.vo.dm.dm104.DelivConvertToOutHVO;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.dm104.DelivbillHVO;
import nc.vo.dm.dm104.OutbillHItemVO;
import nc.vo.dm.dm104.OutbillHVO;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.session.ClientLink;

public class OutBillDlg extends UIDialog
  implements ListSelectionListener, BillEditListener, MouseListener
{
  protected Integer BD301;
  protected Integer BD501;
  protected Integer BD502;
  protected Integer BD503;
  protected Integer BD505;
  protected UFBoolean DM010;
  private boolean handlingAnotherHeadRow = false;

  private DMBillCardPanel ivjBillCardPanel = null;

  private DMBillListPanel ivjBillListPanel = null;

  IvjEventHandler ivjEventHandler = new IvjEventHandler();

  private UIButton ivjUIButtonCancel = null;

  private UIButton ivjUIButtonOk = null;

  private UIButton ivjUIButtonPrint = null;

  private JPanel ivjUIDialogContentPane = null;

  private UIPanel ivjUIPanel1 = null;

  UFDouble[] m_delivbillinvnum = null;

  private DelivbillHHeaderVO m_delivHvo = new DelivbillHHeaderVO();

  private Hashtable m_htDispatcherForCbiztype = new Hashtable();

  private Hashtable m_htVOKeys = new Hashtable();
  private OutbillHVO[] m_hvos;
  protected int m_iFirstSelectCol = -1;

  protected int m_iFirstSelectRow = -1;
  protected String[] m_itemANumKeys;
  protected String m_itemFactorKey;
  protected String[] m_itemNumKeys;
  protected String[] m_itemPriceKeys;
  private int m_selectedListHeadRow;
  public String m_outbillts = null;

  private String m_sBillTypeCode = DMBillTypeConst.m_delivCheckBill;
  protected String m_sCorpID;
  protected String m_sUserID;
  protected String m_sUserName;
  protected String m_sCorpName;
  protected String m_sDelivOrgPK;
  protected String m_sDelivOrgCode;
  protected String m_sDelivOrgName;
  private String m_sNodeCode = "40140412";

  private String m_sTitle = NCLangRes.getInstance().getStrByID("40140408", "UPT40140408-000068");

  private UFDate m_today = null;
  private DelivbillHItemVO[] delivItems;

  public OutBillDlg(Container parent, String title, DMDataVO dmdvo)
  {
    super(parent, title);

    setDelivOrgPK((String)dmdvo.getAttributeValue("pkdelivorg"));
    setUserID((String)dmdvo.getAttributeValue("userid"));
    setUserName((String)dmdvo.getAttributeValue("username"));
    setCorpID((String)dmdvo.getAttributeValue("corpid"));
    setCorpName((String)dmdvo.getAttributeValue("corpname"));

    this.BD501 = ((Integer)dmdvo.getAttributeValue("BD501"));
    this.BD502 = ((Integer)dmdvo.getAttributeValue("BD502"));
    this.BD503 = ((Integer)dmdvo.getAttributeValue("BD503"));
    this.BD505 = ((Integer)dmdvo.getAttributeValue("BD505"));
    this.BD301 = ((Integer)dmdvo.getAttributeValue("BD301"));
    this.DM010 = ((UFBoolean)dmdvo.getAttributeValue("DM010"));

    initialize();
  }

  public void afterEdit(BillEditEvent e)
  {
    String strColName = e.getKey().trim();

    if ((this.handlingAnotherHeadRow) || (strColName.equals("doutnum"))) {
      this.handlingAnotherHeadRow = false;

      Object outnum = null;
      UFDouble totalnum = new UFDouble(0);

      for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); i++) {
        outnum = getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum");

        if (outnum == null) {
          ((OutbillHItemVO)(OutbillHItemVO)this.m_hvos[this.m_selectedListHeadRow].getChildrenVO()[i]).setDoutnum(null);
        }
        else {
          UFDouble ufd = outnum == null ? new UFDouble(0) : new UFDouble(outnum.toString());
          ((OutbillHItemVO)(OutbillHItemVO)this.m_hvos[this.m_selectedListHeadRow].getChildrenVO()[i]).setDoutnum(ufd);
          totalnum = totalnum.add(ufd);
        }

      }

      getBillListPanel().getHeadBillModel().setValueAt(totalnum, this.m_selectedListHeadRow, "dtotalout");

      return;
    }
  }

  public void setDelivbillHItemVO(DelivbillHItemVO[] delivItems)
  {
    this.delivItems = delivItems;
  }

  public void bodyRowChange(BillEditEvent e)
  {
  }

  protected void clearListRow()
  {
    this.m_selectedListHeadRow = -1;
  }

  private void connEtoC1(ActionEvent arg1)
  {
    try
    {
      onOk(arg1);
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }
  }

  private void connEtoC4(ActionEvent arg1)
  {
    try
    {
      onCancel(arg1);
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }
  }

  private void connEtoC5(ActionEvent arg1)
  {
    try
    {
      onTotal(arg1);
    }
    catch (Throwable ivjExc)
    {
      handleException(ivjExc);
    }
  }

  protected Hashtable constructOutbill()
  {
    Hashtable outbillHash = new Hashtable();
    this.m_htVOKeys = new Hashtable();
    try
    {
      OutbillHItemVO[] items = null;
      DelivbillHItemVO delivbillitem = null;
      String pkcust = null; String pkinv = null; String pkwarehouse = null; String pkcorp = null; String ccalbodyid = null; String cbiztype = null; String pkoperator = null; String pkoprdepart = null; String creceiptcorpid = null;
      String key = null;
      String itemkey = null;
      ArrayList outbillArraylist = null;

      for (int i = 0; i < this.m_hvos.length; i++)
      {
        delivbillitem = (DelivbillHItemVO)this.m_hvos[i].getParentVO();

        UFDouble nNum = delivbillitem.getDsignnum();
        UFDouble nAssistNum = delivbillitem.getDinvassist();
        UFDouble nExchange = null;
        if ((nNum != null) && (nAssistNum != null) && (nAssistNum.doubleValue() != 0.0D)) {
          nExchange = new UFDouble(nNum.doubleValue() / nAssistNum.doubleValue());
        }

        pkcust = delivbillitem.getPkcusmandoc();
        pkinv = delivbillitem.getPkinv();
        pkcorp = delivbillitem.getPksalecorp();
        ccalbodyid = delivbillitem.getPksendstockorg();
        cbiztype = delivbillitem.getAttributeValue("cbiztype") == null ? "                    " : delivbillitem.getAttributeValue("cbiztype").toString();

        creceiptcorpid = delivbillitem.getAttributeValue("creceiptcorpid") == null ? "                    " : delivbillitem.getAttributeValue("creceiptcorpid").toString();

        if (delivbillitem.getAttributeValue("pkoperator") != null)
          pkoperator = delivbillitem.getAttributeValue("pkoperator").toString();
        if (delivbillitem.getAttributeValue("pkoprdepart") != null)
          pkoprdepart = delivbillitem.getAttributeValue("pkoprdepart").toString();
        String vdestaddress = null;
        if (delivbillitem.getAttributeValue("vdestaddress") != null) {
          vdestaddress = delivbillitem.getAttributeValue("vdestaddress").toString();
        }

        items = (OutbillHItemVO[])(OutbillHItemVO[])this.m_hvos[i].getChildrenVO();

        if (items == null) {
          continue;
        }
        for (int j = 0; j < items.length; j++)
        {
          Object oOutNum = items[j].getDoutnum();
          if ((oOutNum == null) || (oOutNum.toString().trim().length() == 0) || (((UFDouble)oOutNum).doubleValue() == 0.0D))
          {
            continue;
          }

          DMDataVO dmdvo = new DMDataVO();
          pkwarehouse = items[j].getPkstroe();
          dmdvo.setAttributeValue("pkwarehouse", pkwarehouse);

          dmdvo.setAttributeValue("cbiztype", cbiztype);
          if (vdestaddress != null) {
            dmdvo.setAttributeValue("vdestaddress", vdestaddress);
          }
          if (creceiptcorpid != null) {
            dmdvo.setAttributeValue("creceiptcorpid", creceiptcorpid);
          }
          key = pkwarehouse + cbiztype;
          if (pkcust != null) {
            key = key + pkcust;
            dmdvo.setAttributeValue("pkcust", pkcust);
          }
          if (pkcorp != null) {
            key = key + pkcorp;
            dmdvo.setAttributeValue("pkcorp", pkcorp);
          }
          if (ccalbodyid != null) {
            key = key + ccalbodyid;
            dmdvo.setAttributeValue("ccalbodyid", ccalbodyid);
          }

          if (pkoperator != null) {
            key = key + pkoperator;
            dmdvo.setAttributeValue("pkoperator", pkoperator);
          }
          if (pkoprdepart != null) {
            key = key + pkoprdepart;
            dmdvo.setAttributeValue("pkoprdepart", pkoprdepart);
          }

          dmdvo.setAttributeValue("cquoteunitid", delivbillitem.getAttributeValue("cquoteunitid"));
          dmdvo.setAttributeValue("nquoteunitrate", delivbillitem.getAttributeValue("nquoteunitrate"));
          dmdvo.setAttributeValue("nquoteunitnum", delivbillitem.getAttributeValue("nquoteunitnum"));

          this.m_htVOKeys.put(key, dmdvo);

          DelivConvertToOutHItemVO deliv2OutVO = new DelivConvertToOutHItemVO();

          deliv2OutVO.setAttributeValue("dinvnum", oOutNum);

          if ((nExchange != null) && (nExchange.doubleValue() != 0.0D)) {
            double d = ((UFDouble)oOutNum).doubleValue() / nExchange.doubleValue();

            deliv2OutVO.setAttributeValue("dinvassist", new UFDouble(d));
          }

          deliv2OutVO.setAttributeValue("pkdestrep", delivbillitem.getPkdestrep());

          deliv2OutVO.setAttributeValue("pk_delivbill_b", delivbillitem.getPk_delivbill_b());

          deliv2OutVO.setAttributeValue("pk_delivbill_h", delivbillitem.getPk_delivbill_h());

          deliv2OutVO.setAttributeValue("pkorderrow", delivbillitem.getPkorderrow());

          deliv2OutVO.setAttributeValue("pkorder", delivbillitem.getPkorder());

          deliv2OutVO.setAttributeValue("vbilltype", delivbillitem.getVbilltype());

          deliv2OutVO.setAttributeValue("csourcetype", DMBillTypeConst.m_delivDelivBill);

          deliv2OutVO.setAttributeValue("vdelivbillcode", this.m_delivHvo.getVdelivbillcode());

          deliv2OutVO.setAttributeValue("pkinv", delivbillitem.getPkinv());

          deliv2OutVO.setAttributeValue("pkassistmeasure", delivbillitem.getPkassistmeasure());

          deliv2OutVO.setAttributeValue("vfree1", delivbillitem.getVfree1());

          deliv2OutVO.setAttributeValue("vfree2", delivbillitem.getVfree2());

          deliv2OutVO.setAttributeValue("vfree3", delivbillitem.getVfree3());

          deliv2OutVO.setAttributeValue("vfree4", delivbillitem.getVfree4());

          deliv2OutVO.setAttributeValue("vfree5", delivbillitem.getVfree5());

          deliv2OutVO.setAttributeValue("vfree6", delivbillitem.getVfree6());

          deliv2OutVO.setAttributeValue("vfree7", delivbillitem.getVfree7());

          deliv2OutVO.setAttributeValue("vfree8", delivbillitem.getVfree8());

          deliv2OutVO.setAttributeValue("vfree9", delivbillitem.getVfree9());

          deliv2OutVO.setAttributeValue("vfree10", delivbillitem.getVfree10());

          for (int r = 0; r < 20; r++)
          {
            deliv2OutVO.setAttributeValue("vuserdef" + r, delivbillitem.getAttributeValue("vuserdef" + r));

            deliv2OutVO.setAttributeValue("pk_defdoc" + r, delivbillitem.getAttributeValue("pk_defdoc" + r));
          }

          deliv2OutVO.setAttributeValue("vbatchcode", delivbillitem.getVbatchcode());

          deliv2OutVO.setAttributeValue("blargess", delivbillitem.getBlargess());
          deliv2OutVO.setAttributeValue("vnote", delivbillitem.getAttributeValue("vnote"));

          deliv2OutVO.setAttributeValue("irownumber", delivbillitem.getIrownumber());

          if (delivbillitem.getAttributeValue("creceiptcorpid") != null)
          {
            deliv2OutVO.setAttributeValue("creceiptcorpid", delivbillitem.getAttributeValue("creceiptcorpid").toString());
          }

          if (delivbillitem.getAttributeValue("vdestaddress") != null) {
            deliv2OutVO.setAttributeValue("vdestaddress", delivbillitem.getAttributeValue("vdestaddress").toString());
          }

          deliv2OutVO.setAttributeValue("cquoteunitid", delivbillitem.getAttributeValue("cquoteunitid"));
          deliv2OutVO.setAttributeValue("nquoteunitrate", delivbillitem.getAttributeValue("nquoteunitrate"));
          deliv2OutVO.setAttributeValue("nquoteunitnum", delivbillitem.getAttributeValue("nquoteunitnum"));

          if (delivbillitem.getAttributeValue("creceiptcorp") != null)
          {
            deliv2OutVO.setAttributeValue("creceiptcorp", delivbillitem.getAttributeValue("creceiptcorp").toString());
          }

          deliv2OutVO.setAttributeValue("pkitem", delivbillitem.getAttributeValue("pkitem"));
          deliv2OutVO.setAttributeValue("pkitemperiod", delivbillitem.getAttributeValue("pkitemperiod"));

          deliv2OutVO.setAttributeValue("pkcusmandoc", delivbillitem.getAttributeValue("pkcust"));

          if (!outbillHash.containsKey(key))
          {
            outbillArraylist = new ArrayList();
          }
          else
          {
            outbillArraylist = (ArrayList)outbillHash.get(key);
          }
          outbillArraylist.add(deliv2OutVO);

          outbillHash.put(key, outbillArraylist);
        }
      }
    }
    catch (Exception e)
    {
      reportException(e);
    }
    return outbillHash;
  }

  protected DelivConvertToOutHVO[] genGeneralBillVOs(Hashtable outbillHash)
    throws Exception
  {
    String pkcust = null; String pkwhs = null; String pkinv = null; String vbilltype = null; String pkcorp = null; String ccalbodyid = null; String cbiztype = null; String cfirsttype = null;

    DelivConvertToOutHVO[] dlvcvthvos = null;

    DelivConvertToOutHVO deliv2OutVO = null;

    DelivConvertToOutHVO otherdlvcvthvo = null;
    GeneralBillHeaderVO gnrlheader = null;
    DelivConvertToOutHHeaderVO dlvcvtheader = null;
    GeneralBillHeaderVO otherheader = null;
    DelivConvertToOutHHeaderVO otherdlvcvtheader = null;

    DelivConvertToOutHItemVO[] dlvcvtitems = null;

    DelivConvertToOutHItemVO[] otherdlvcvtitems = null;
    GeneralBillItemVO gnrlitem = null;
    DelivConvertToOutHItemVO deliv2OutItemVO = null;

    Vector dlvcvtVector = new Vector();
    Vector dlvcvtItemVector = new Vector();
    Vector otherdlvcvtItemVector = new Vector();

    Enumeration enumr = null;

    enumr = outbillHash.keys();
    String key = null;
    ArrayList icbillitems = null;
    String[] value = null;
    while (enumr.hasMoreElements())
    {
      deliv2OutVO = new DelivConvertToOutHVO();

      otherdlvcvthvo = new DelivConvertToOutHVO();

      key = (String)enumr.nextElement();
      gnrlheader = new GeneralBillHeaderVO();
      dlvcvtheader = new DelivConvertToOutHHeaderVO();

      DMDataVO dmdvo = (DMDataVO)this.m_htVOKeys.get(key);
      if (null == dmdvo)
        continue;
      pkwhs = (String)dmdvo.getAttributeValue("pkwarehouse");
      cbiztype = (String)dmdvo.getAttributeValue("cbiztype");
      pkcust = (String)dmdvo.getAttributeValue("pkcust");
      pkcorp = (String)dmdvo.getAttributeValue("pkcorp");
      ccalbodyid = (String)dmdvo.getAttributeValue("ccalbodyid");

      String pkoperator = (String)dmdvo.getAttributeValue("pkoperator");
      String pkoprdepart = (String)dmdvo.getAttributeValue("pkoprdepart");

      dlvcvtheader.setAttributeValue("vdelivbillcode", this.m_delivHvo.getVdelivbillcode());
      gnrlheader.setAttributeValue("coperatorid", getUserID());
      dlvcvtheader.setAttributeValue("userid", getUserID());
      gnrlheader.setAttributeValue("dbilldate", getClientEnvironment().getDate());
      dlvcvtheader.setAttributeValue("billdate", getClientEnvironment().getDate());
      gnrlheader.setCcustomerid(pkcust);
      gnrlheader.setCwarehouseid(pkwhs);
      gnrlheader.setPk_corp(pkcorp);
      gnrlheader.setPk_calbody(ccalbodyid);
      gnrlheader.setCbiztypeid(cbiztype);
      gnrlheader.setCdispatcherid(getDispatcherForCbiztype(cbiztype));
      gnrlheader.setCbilltypecode(BillTypeConst.m_saleOut);

      gnrlheader.setAttributeValue("cwastewarehouseid", this.m_delivHvo.getAttributeValue("pktrancust"));
      dlvcvtheader.setAttributeValue("pktrancust", this.m_delivHvo.getAttributeValue("pktrancust"));

      gnrlheader.setCbizid(pkoperator);
      dlvcvtheader.setAttributeValue("pkoperator", pkoperator);
      gnrlheader.setCdptid(pkoprdepart);
      dlvcvtheader.setAttributeValue("pkoprdepart", pkoprdepart);

      gnrlheader.setCdilivertypeid(this.m_delivHvo.getPkdelivmode());
      dlvcvtheader.setAttributeValue("pkdelivmode", this.m_delivHvo.getPkdelivmode());

      if (dmdvo.getAttributeValue("vdestaddress") != null) {
        gnrlheader.setVdiliveraddress(dmdvo.getAttributeValue("vdestaddress").toString());
      }

      for (int j = 0; j < 20; j++) {
        gnrlheader.setAttributeValue("vuserdef" + (j + 1), this.m_delivHvo.getAttributeValue("vuserdef" + j));
        dlvcvtheader.setAttributeValue("vuserdef" + j, this.m_delivHvo.getAttributeValue("vuserdef" + j));

        gnrlheader.setAttributeValue("pk_defdoc" + (j + 1), this.m_delivHvo.getAttributeValue("pk_defdoc" + j));
        dlvcvtheader.setAttributeValue("pk_defdoc" + j, this.m_delivHvo.getAttributeValue("pk_defdoc" + j));
      }

      otherheader = (GeneralBillHeaderVO)gnrlheader.clone();
      otherdlvcvtheader = new DelivConvertToOutHHeaderVO();
      dlvcvtheader.translateToOtherVO(otherdlvcvtheader);
      otherheader.setCbilltypecode(BillTypeConst.m_otherOut);

      deliv2OutVO.setParentVO(dlvcvtheader);

      otherdlvcvthvo.setParentVO(otherdlvcvtheader);

      icbillitems = (ArrayList)outbillHash.get(key);

      dlvcvtItemVector = new Vector();
      otherdlvcvtItemVector = new Vector();

      if (icbillitems.size() == 0)
        continue;
      for (int i = 0; i < icbillitems.size(); i++) {
        deliv2OutItemVO = null;
        deliv2OutItemVO = (DelivConvertToOutHItemVO)icbillitems.get(i);

        deliv2OutItemVO.setAttributeValue("pkoperator", pkoperator);
        deliv2OutItemVO.setAttributeValue("pkoprdepart", pkoprdepart);
        deliv2OutItemVO.setAttributeValue("pkcusmandoc", pkcust);
        deliv2OutItemVO.setAttributeValue("pkstroe", pkwhs);
        deliv2OutItemVO.setAttributeValue("pksalecorp", pkcorp);
        deliv2OutItemVO.setAttributeValue("pksendstockorg", ccalbodyid);
        deliv2OutItemVO.setAttributeValue("cbiztype", cbiztype);
        deliv2OutItemVO.setAttributeValue("cdispatcherid", getDispatcherForCbiztype(cbiztype));
        deliv2OutItemVO.setAttributeValue("voutbilltype", BillTypeConst.m_saleOut);

        deliv2OutItemVO.setAttributeValue("cquoteunitid", dmdvo.getAttributeValue("cquoteunitid"));
        deliv2OutItemVO.setAttributeValue("nquoteunitrate", dmdvo.getAttributeValue("nquoteunitrate"));
        deliv2OutItemVO.setAttributeValue("nquoteunitnum", dmdvo.getAttributeValue("nquoteunitnum"));

        if (deliv2OutItemVO.getAttributeValue("csourcetype") != null)
          vbilltype = deliv2OutItemVO.getAttributeValue("csourcetype").toString();
        else
          vbilltype = null;
        if (deliv2OutItemVO.getAttributeValue("vbilltype") != null)
          cfirsttype = deliv2OutItemVO.getAttributeValue("vbilltype").toString();
        else {
          cfirsttype = null;
        }
        if ((null != cfirsttype) && (cfirsttype.equals("30"))) {
          dlvcvtItemVector.add(deliv2OutItemVO);
        } else if ((null != cfirsttype) && (cfirsttype.equals(BillTypeConst.m_AllocationOrder))) {
          deliv2OutItemVO.setAttributeValue("voutbilltype", BillTypeConst.m_otherOut);
          otherdlvcvtItemVector.add(deliv2OutItemVO);
        }
        else if ((null != cfirsttype) && (cfirsttype.equals("5I"))) {
          deliv2OutItemVO.setAttributeValue("voutbilltype", BillTypeConst.m_allocationOut);
          otherdlvcvtItemVector.add(deliv2OutItemVO);
        }
        else if ((null != cfirsttype) && (cfirsttype.equals("5C"))) {
          deliv2OutItemVO.setAttributeValue("voutbilltype", BillTypeConst.m_allocationOut);
          otherdlvcvtItemVector.add(deliv2OutItemVO);
        }
        else if ((null != cfirsttype) && (cfirsttype.equals("5D"))) {
          deliv2OutItemVO.setAttributeValue("voutbilltype", BillTypeConst.m_allocationOut);
          otherdlvcvtItemVector.add(deliv2OutItemVO);
        }
        else if ((null != cfirsttype) && (cfirsttype.equals("5E"))) {
          deliv2OutItemVO.setAttributeValue("voutbilltype", BillTypeConst.m_allocationOut);
          otherdlvcvtItemVector.add(deliv2OutItemVO);
        }

      }

      if (dlvcvtItemVector.size() > 0) {
        dlvcvtitems = new DelivConvertToOutHItemVO[dlvcvtItemVector.size()];
        dlvcvtItemVector.copyInto(dlvcvtitems);
        deliv2OutVO.setChildrenVO(dlvcvtitems);
      }
      else {
        deliv2OutVO.setChildrenVO(null);
      }

      if (otherdlvcvtItemVector.size() > 0) {
        otherdlvcvtitems = new DelivConvertToOutHItemVO[otherdlvcvtItemVector.size()];
        otherdlvcvtItemVector.copyInto(otherdlvcvtitems);
        otherdlvcvthvo.setChildrenVO(otherdlvcvtitems);
      }
      else {
        otherdlvcvthvo.setChildrenVO(null);
      }

      if ((deliv2OutVO.getChildrenVO() != null) && (deliv2OutVO.getChildrenVO().length != 0)) {
        dlvcvtVector.add(deliv2OutVO);
      }

      if ((otherdlvcvthvo.getChildrenVO() != null) && (otherdlvcvthvo.getChildrenVO().length != 0)) {
        dlvcvtVector.add(otherdlvcvthvo);
      }

    }

    dlvcvthvos = new DelivConvertToOutHVO[dlvcvtVector.size()];

    dlvcvtVector.copyInto(dlvcvthvos);

    return dlvcvthvos;
  }

  protected DMBillListPanel getBillListPanel()
  {
    if (this.ivjBillListPanel == null) {
      try {
        this.ivjBillListPanel = new DMBillListPanel(false);
        this.ivjBillListPanel.setName("BillCardPanel");

        BillListData listdata = new BillListData(this.ivjBillListPanel.getTempletData("DM_BILL_TEMPLET_007U"));

        this.m_itemNumKeys = new String[] { "dinvnum", "dtotalout", "doutnum", "donhandnum" };
        ArrayList alDecimalKey = new ArrayList();
        alDecimalKey.add(this.m_itemNumKeys);

        ArrayList alPrecision = new ArrayList();
        alPrecision.add(this.BD501);

        this.ivjBillListPanel.changeDecimalItemsPrecision(listdata, alDecimalKey, alPrecision);

        this.ivjBillListPanel.setListData(listdata);

        this.ivjBillListPanel.getParentListPanel().removeTableSortListener();
        this.ivjBillListPanel.getBodyScrollPane("table").removeTableSortListener();

        this.ivjBillListPanel.setCorp(getCorpID());

        this.ivjBillListPanel.setOperator(getUserID());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillListPanel;
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
  }

  public String getCorpName()
  {
    return this.m_sCorpName;
  }

  public String getDelivOrgCode()
  {
    return this.m_sDelivOrgCode;
  }

  public String getDelivOrgName()
  {
    return this.m_sDelivOrgName;
  }

  public String getDelivOrgPK()
  {
    return this.m_sDelivOrgPK;
  }

  protected String getDispatcherForCbiztype(String sCbiztype)
    throws Exception
  {
    try
    {
      if (this.m_htDispatcherForCbiztype.containsKey(sCbiztype.trim())) {
        return this.m_htDispatcherForCbiztype.get(sCbiztype.trim()).toString();
      }
      String sRecptType = DelivbillHBO_Client.queryDispatcherForCbiztype(sCbiztype.trim());
      if ((null != sRecptType) && (sRecptType.trim().length() != 0)) {
        this.m_htDispatcherForCbiztype.put(sCbiztype.trim(), sRecptType);
        return sRecptType;
      }

    }
    catch (Exception e)
    {
      Exception ee = new Exception(NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000101") + e.getMessage());

      throw ee;
    }

    return "";
  }

  /** @deprecated */
  public String getDlvbillTs()
  {
    return this.m_outbillts;
  }

  public DelivbillHHeaderVO getHvo()
  {
    return this.m_delivHvo;
  }

  private UIButton getUIButtonCancel()
  {
    if (this.ivjUIButtonCancel == null) {
      try {
        this.ivjUIButtonCancel = new UIButton();
        this.ivjUIButtonCancel.setName("UIButtonCancel");
        this.ivjUIButtonCancel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonCancel;
  }

  private UIButton getUIButtonOk()
  {
    if (this.ivjUIButtonOk == null) {
      try {
        this.ivjUIButtonOk = new UIButton();
        this.ivjUIButtonOk.setName("UIButtonOk");
        this.ivjUIButtonOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonOk;
  }

  private UIButton getUIButtonPrint()
  {
    if (this.ivjUIButtonPrint == null) {
      try {
        this.ivjUIButtonPrint = new UIButton();
        this.ivjUIButtonPrint.setName("UIButtonPrint");
        this.ivjUIButtonPrint.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001146"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonPrint;
  }

  protected JPanel getUIDialogContentPane()
  {
    if (this.ivjUIDialogContentPane == null) {
      try {
        this.ivjUIDialogContentPane = new JPanel();
        this.ivjUIDialogContentPane.setName("UIDialogContentPane");
        this.ivjUIDialogContentPane.setLayout(new BorderLayout());
        getUIDialogContentPane().add(getBillListPanel(), "Center");
        getUIDialogContentPane().add(getUIPanel1(), "South");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIDialogContentPane;
  }

  private UIPanel getUIPanel1()
  {
    if (this.ivjUIPanel1 == null) {
      try {
        this.ivjUIPanel1 = new UIPanel();
        this.ivjUIPanel1.setName("UIPanel1");
        this.ivjUIPanel1.setMinimumSize(new Dimension(100, 100));
        getUIPanel1().add(getUIButtonOk(), getUIButtonOk().getName());
        getUIPanel1().add(getUIButtonCancel(), getUIButtonCancel().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanel1;
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }

  public String getUserName()
  {
    return this.m_sUserName;
  }

  private void handleException(Throwable exception)
  {
  }

  private void initConnections()
    throws Exception
  {
    getUIButtonOk().addActionListener(this.ivjEventHandler);
    getUIButtonPrint().addActionListener(this.ivjEventHandler);
    getUIButtonCancel().addActionListener(this.ivjEventHandler);
  }

  private void initialize()
  {
    try
    {
      this.m_sBillTypeCode = "7U";

      setName("OutBillDlg");
      setDefaultCloseOperation(2);
      setSize(710, 500);
      setModal(true);
      setContentPane(getUIDialogContentPane());
      initConnections();
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getBillListPanel().setEnabled(true);
    getBillListPanel().getBodyTable().setEnabled(true);
    getBillListPanel().getHeadTable().getSelectionModel().addListSelectionListener(this);

    getBillListPanel().addBodyEditListener(this);
    getBillListPanel().getBodyTable().addMouseListener(this);

    UIRefPane nRefPane = (UIRefPane)getBillListPanel().getBodyItem("doutnum").getComponent();
    UITextField nUI = nRefPane.getUITextField();
    nUI.setDelStr("-");
  }

  public void listHeadRowChange(BillEditEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void onCancel(ActionEvent actionEvent)
  {
    setResult(2);
    close();
    fireUIDialogClosed(new UIDialogEvent(this, 202));
  }

  private void setNoutassistnum(GeneralBillVO[] gnrlbillvos)
  {
    for (int i = 0; i < gnrlbillvos.length; i++) {
      GeneralBillItemVO[] outBillItems = (GeneralBillItemVO[])(GeneralBillItemVO[])gnrlbillvos[i].getChildrenVO();
      for (int j = 0; j < outBillItems.length; j++)
        for (int k = 0; k < this.delivItems.length; k++)
          if (this.delivItems[k].getPk_delivbill_b().equals(outBillItems[j].getAttributeValue("csourcebillbid"))) {
            UFDouble nExchange = null;

            UFDouble nNum = this.delivItems[k].getDinvnum();
            UFDouble nAssistNum = this.delivItems[k].getDinvassist();
            if ((nNum != null) && (nAssistNum != null) && (nAssistNum.doubleValue() != 0.0D)) {
              nExchange = new UFDouble(nNum.doubleValue() / nAssistNum.doubleValue());
            }

            if (nExchange == null) break;
            UFDouble ufdoutnum = outBillItems[j].getNshouldoutnum();
            if ((ufdoutnum != null) && (ufdoutnum.doubleValue() != 0.0D)) {
              UFDouble d = ufdoutnum.div(nExchange);
              outBillItems[j].setAttributeValue("nshouldoutassistnum", d);
              outBillItems[j].setHsl(nExchange);
            }
            break;
          }
    }
  }

  public void onOk(ActionEvent actionEvent)
  {
    try
    {
      OutbillHItemVO[] items = null;

      GeneralBillVO[] gnrlbillvos = null;

      Hashtable outbillHash = null;

      if (this.m_selectedListHeadRow < 0) {
        setResult(2);
        close();
        fireUIDialogClosed(new UIDialogEvent(this, 202));
        return;
      }

      if ((this.m_selectedListHeadRow < this.m_hvos.length) && (this.m_selectedListHeadRow >= 0)) {
        items = (OutbillHItemVO[])(OutbillHItemVO[])this.m_hvos[this.m_selectedListHeadRow].getChildrenVO();
      }

      if (items != null) {
        for (int i = 0; i < items.length; i++) {
          if (i >= getBillListPanel().getBodyBillModel().getRowCount()) {
            continue;
          }
          Object obj = getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum");
          if ((null == obj) || (obj.toString().trim().length() == 0))
            obj = new UFDouble(0);
          items[i].setDoutnum((UFDouble)obj);
        }

      }

      if (!this.DM010.booleanValue())
      {
        for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
          Object ufdTmp = getBillListPanel().getHeadBillModel().getValueAt(i, "dinvnum");
          UFDouble ufdShouldOut = ufdTmp == null ? new UFDouble(0) : (UFDouble)ufdTmp;

          ufdTmp = getBillListPanel().getHeadBillModel().getValueAt(i, "dtotalout");
          UFDouble ufdOut = ufdTmp == null ? new UFDouble(0) : (UFDouble)ufdTmp;
          if (ufdShouldOut.doubleValue() < ufdOut.doubleValue()) {
            MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000076"), NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000099"));

            return;
          }
        }

      }

      if (!checkOnHand()) {
        return;
      }

      outbillHash = constructOutbill();

      DelivConvertToOutHVO[] deliv2OutVO = genGeneralBillVOs(outbillHash);

      if (null == deliv2OutVO) {
        return;
      }

      deliv2OutVO = DelivbillHBO_Client.splitVOForICOutBill(deliv2OutVO);
      if (deliv2OutVO.length == 0) {
        return;
      }
      DelivConvertToOutHItemVO[] dlvcvthitems = null;

      gnrlbillvos = new GeneralBillVO[deliv2OutVO.length];
      for (int i = 0; i < deliv2OutVO.length; i++) {
        dlvcvthitems = (DelivConvertToOutHItemVO[])(DelivConvertToOutHItemVO[])deliv2OutVO[i].getChildrenVO();
        deliv2OutVO[i].getParentVO().setStatus(2);
        if ((dlvcvthitems != null) && (dlvcvthitems.length > 0) && (dlvcvthitems[0].getAttributeValue("voutbilltype") != null))
          for (int j = 0; j < dlvcvthitems.length; j++)
            dlvcvthitems[j].setStatus(2);
        if (dlvcvthitems[0].getAttributeValue("voutbilltype").equals(BillTypeConst.m_saleOut))
        {
          gnrlbillvos[i] = ((GeneralBillVO)PfChangeBO_Client.pfChangeBillToBill(deliv2OutVO[i], "7X", "4C"));
        }
        else if (dlvcvthitems[0].getAttributeValue("voutbilltype").equals(BillTypeConst.m_otherOut))
        {
          gnrlbillvos[i] = ((GeneralBillVO)PfChangeBO_Client.pfChangeBillToBill(deliv2OutVO[i], "7X", "4I"));
        } else {
          if (!dlvcvthitems[0].getAttributeValue("voutbilltype").equals(BillTypeConst.m_allocationOut))
            continue;
          gnrlbillvos[i] = ((GeneralBillVO)PfChangeBO_Client.pfChangeBillToBill(deliv2OutVO[i], "7X", "4Y"));
        }

      }

      UFDateTime ufdtAddTime = null;
      try {
        IRecordTime bo = (IRecordTime)NCLocator.getInstance().lookup(IRecordTime.class.getName());
        ufdtAddTime = bo.getTimeStamp();
      }
      catch (Exception e) {
        ufdtAddTime = null;
      }

      DelivbillHVO delivbillVO = new DelivbillHVO();
      delivbillVO.setParentVO(this.m_delivHvo);
      DelivbillHItemVO[] dlvitems = new DelivbillHItemVO[this.m_hvos.length];
      for (int i = 0; i < this.m_hvos.length; i++) {
        dlvitems[i] = ((DelivbillHItemVO)this.m_hvos[i].getParentVO());
        dlvitems[i].setDinvnum(this.m_delivbillinvnum[i]);
      }
      delivbillVO.setChildrenVO(dlvitems);
     
      setNoutassistnum(gnrlbillvos);

      this.m_outbillts = DelivbillHBO_Client.saveOutDM(gnrlbillvos, ufdtAddTime, delivbillVO, new ClientLink(getClientEnvironment()));

      setResult(1);
      close();
      fireUIDialogClosed(new UIDialogEvent(this, 204));
      return;
    }
    catch (Exception e) {
      reportException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40140408", "UPP40140408-000100") + e.getMessage());
    }
  }

  public void onTotal(ActionEvent actionEvent)
  {
    UFDouble outnum = null; UFDouble totalnum = new UFDouble(0);

    for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); i++) {
      outnum = (UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum");
      if (outnum != null) {
        totalnum = totalnum.add(outnum);
      }
    }
    getBillListPanel().getHeadBillModel().setValueAt(totalnum, this.m_selectedListHeadRow, "dtotalout");
  }

  public void setCorpID(String newCorpID)
  {
    this.m_sCorpID = newCorpID;
  }

  public void setCorpName(String newCorpName)
  {
    this.m_sCorpName = newCorpName;
  }

  public void setDelivOrgCode(String newDelivOrgCode)
  {
    this.m_sDelivOrgCode = newDelivOrgCode;
  }

  public void setDelivOrgName(String newDelivOrgName)
  {
    this.m_sDelivOrgName = newDelivOrgName;
  }

  public void setDelivOrgPK(String newDelivOrgPK)
  {
    this.m_sDelivOrgPK = newDelivOrgPK;
  }

  public void setHvo(DelivbillHHeaderVO newHvo)
  {
    this.m_delivHvo = newHvo;
  }

  public void setOutbillHVOs(OutbillHVO[] hvos)
  {
    DelivbillHItemVO[] items = new DelivbillHItemVO[hvos.length];
    this.m_delivbillinvnum = new UFDouble[hvos.length];
    OutbillHItemVO[] outitems = null;
    for (int i = 0; i < hvos.length; i++) {
      items[i] = ((DelivbillHItemVO)hvos[i].getParentVO());
      UFDouble dInvNum = items[i].getDinvnum();
      UFDouble dOutNum = items[i].getDoutnum();
      this.m_delivbillinvnum[i] = dInvNum;
      if ((dInvNum != null) && (dOutNum != null)) {
        double d = dInvNum.doubleValue() - dOutNum.doubleValue();
        items[i].setDinvnum(new UFDouble(d));
      }

      items[i].setAttributeValue("dtotalout", items[i].getDinvnum());
      if (hvos[i].getChildrenVO() != null) {
        ((OutbillHItemVO)hvos[i].getChildrenVO()[0]).setDoutnum(items[i].getDinvnum());
      }

      outitems = (OutbillHItemVO[])(OutbillHItemVO[])hvos[i].getChildrenVO();
      if ((outitems != null) && (outitems.length > 0) && (outitems[0] != null) && (items[i] != null)) {
        outitems[0].setDoutnum(items[i].getDinvnum());
      }
    }
    getBillListPanel().setHeaderValueVO(items);
    this.m_hvos = hvos;
  }

  public void setUserID(String newUserID)
  {
    this.m_sUserID = newUserID;
  }

  public void setUserName(String newUserName)
  {
    this.m_sUserName = newUserName;
  }

  public void valueChanged(ListSelectionEvent e)
  {
    if (!e.getValueIsAdjusting()) {
      this.m_selectedListHeadRow = getBillListPanel().getHeadTable().getSelectedRow();

      OutbillHItemVO[] items = null;

      if ((this.m_selectedListHeadRow < this.m_hvos.length) && (this.m_selectedListHeadRow >= 0)) {
        items = (OutbillHItemVO[])(OutbillHItemVO[])this.m_hvos[this.m_selectedListHeadRow].getChildrenVO();
      }

      getBillListPanel().getBodyBillModel().clearTotalModel();
      getBillListPanel().setBodyValueVO(items);
    }
    else if ((this.m_selectedListHeadRow < this.m_hvos.length) && (this.m_selectedListHeadRow >= 0))
    {
      this.handlingAnotherHeadRow = true;
    }
  }

  private ClientEnvironment getClientEnvironment() {
    return ClientEnvironment.getInstance();
  }

  private boolean checkOnHand() {
    Map outIndex = new HashMap();
    Map onhandIndex = new HashMap();
    for (int i = 0; i < this.m_hvos.length; i++) {
      OutbillHItemVO[] items = (OutbillHItemVO[])(OutbillHItemVO[])this.m_hvos[i].getChildrenVO();
      if (items == null) {
        continue;
      }
      DelivbillHItemVO header = (DelivbillHItemVO)this.m_hvos[i].getParentVO();
      String pk_inv = header.getPkinv();
      for (int j = 0; j < items.length; j++) {
        UFDouble outnum = items[j].getDoutnum();
        UFDouble onhandnum = items[j].getDonhandnum();
        String pkstordoc = items[j].getPkstroe();
        if ((outnum != null) && (outnum.doubleValue() != 0.0D)) {
          String key = pkstordoc + pk_inv;
          if (outIndex.containsKey(key)) {
            UFDouble allnum = (UFDouble)outIndex.get(key);
            allnum = allnum.add(outnum);
            outIndex.put(key, allnum);
          }
          else {
            outIndex.put(key, outnum);
          }
          if (!onhandIndex.containsKey(key)) {
            onhandIndex.put(key, onhandnum);
          }
        }
      }
    }
    Iterator iterator = outIndex.keySet().iterator();
    while (iterator.hasNext()) {
      Object key = iterator.next();
      UFDouble outnum = (UFDouble)outIndex.get(key);
      UFDouble onhandnum = (UFDouble)onhandIndex.get(key);
      if ((onhandnum == null) || (outnum.doubleValue() > onhandnum.doubleValue()))
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000052"), NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000077"));

        return false;
      }
    }
    return true;
  }

  class IvjEventHandler
    implements ActionListener
  {
    IvjEventHandler()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == OutBillDlg.this.getUIButtonOk())
        OutBillDlg.this.connEtoC1(e);
      if (e.getSource() == OutBillDlg.this.getUIButtonCancel())
        OutBillDlg.this.connEtoC4(e);
      if (e.getSource() == OutBillDlg.this.getUIButtonPrint())
        OutBillDlg.this.connEtoC5(e);
    }
  }
}