package nc.ui.ic.ic207;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.pub.SCMEnv;



public class ClientUI extends GeneralBillClientUI
{
  private boolean m_bIsOutBill = false;
  private UITextField m_uitfHinttext = new UITextField();

  public ClientUI() {
    initialize();
  }

  public ClientUI(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    super(pk_corp, billType, businessType, operator, billID);
  }

  protected void afterBillEdit(BillEditEvent e) {
    if ((getParentCorpCode().equals("10395")) && (
      (e.getKey().equals("cwarehouseid")) || 
      (e.getKey().equals("cinventorycode")))) {
      String cwarehouseid = ((UIRefPane)getBillCardPanel()
        .getHeadItem("cwarehouseid").getComponent()).getRefPK();

      if ((cwarehouseid == null) || (cwarehouseid.equals(""))) {
        return;
      }
      String sCode = ((UIRefPane)getBillCardPanel().getHeadItem(
        "cwarehouseid").getComponent()).getRefCode();

      if (Iscsflag(cwarehouseid))
      {
        int i = 0;
        while (i < getBillCardPanel().getBillTable()
          .getRowCount())
        {
          String cinventoryid = (String)getBillCardPanel()
            .getBodyValueAt(i, "cinventoryid");
          if ((cinventoryid == null) || (cinventoryid.equals(""))) {
            return;
          }

          String Sql = "select * from (select d.pk_cargdoc,d.csname,d.cscode from ic_general_h a  ";
          Sql = Sql + "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
          Sql = Sql + "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";
          Sql = Sql + "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc  ";
          Sql = Sql + "where a.cbilltypecode ='4A' and a.dr=0 and a.cwarehouseid='" + 
            cwarehouseid + 
            "' and  b.cinventoryid='" + 
            cinventoryid + "'  ";
          Sql = Sql + "and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
          Sql = Sql + ") where rownum=1  ";
          IUAPQueryBS sessionManager = (IUAPQueryBS)
            NCLocator.getInstance().lookup(
            IUAPQueryBS.class.getName());
          List list = null;
          try {
            list = (List)sessionManager.executeQuery(Sql, 
              new ArrayListProcessor());

            if (list.isEmpty())
            {
              Sql = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
              Sql = Sql + "from   v_ic_onhandnum6 kp  ";
              Sql = Sql + "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
              Sql = Sql + "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null and kp.cwarehouseid='" + 
                cwarehouseid + 
                "'  and  kp.cinventoryid='" + 
                cinventoryid + "')  ";
              Sql = Sql + "where rownum=1  ";
              list = (List)sessionManager.executeQuery(Sql, 
                new ArrayListProcessor());
              if (list.isEmpty()) {
                return;
              }
            }
          }
          catch (BusinessException e1)
          {
            e1.printStackTrace();

            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
              ArrayList values = new ArrayList();
              Object obj = iterator.next();
              if (obj == null) {
                continue;
              }
              if (obj.getClass().isArray()) {
                int len = Array.getLength(obj);
                for (int j = 0; j < len; j++) {
                  values.add(Array.get(obj, j));
                }
              }

              setSpace(String.valueOf(values.get(0)), 
                String.valueOf(values.get(1)), i);
            }
            i++;
          }
        }
      }
    }
  }

  protected void afterBillItemSelChg(int iRow, int iCol)
  {
  }

  public void afterWhEditNoClearCalbody(BillEditEvent e)
  {
    try
    {
      String sNewWhID = ((GeneralBillHeaderVO)this.m_voBill
        .getParentVO()).getCwarehouseid();

      if (sNewWhID == null) {
        getLotNumbRefPane().setWHParams(null);
      } else {
        int iQryMode = 1;

        Object oParam = sNewWhID;

        ArrayList alAllInvID = new ArrayList();
        boolean bHaveInv = getCurInvID(alAllInvID);

        WhVO voWh = null;

        getLotNumbRefPane().setWHParams(null);
        if (this.m_voBill != null) {
          this.m_voBill.setWh(null);
        }
        if (bHaveInv) {
          ArrayList alParam = new ArrayList();
          alParam.add(sNewWhID);
          iQryMode = 15;

          if ((this.m_voBill != null) && 
            (this.m_voBill.getWh() != null))
            alParam.add(this.m_voBill.getWh().getPk_calbody());
          else {
            alParam.add(null);
          }
          alParam.add(this.m_sCorpID);

          alParam.add(alAllInvID);
          oParam = alParam;
        }

        Object oRet = GeneralBillHelper.queryInfo(
          new Integer(iQryMode), oParam);

        if ((oRet instanceof ArrayList)) {
          ArrayList alRetValue = (ArrayList)oRet;
          if ((alRetValue != null) && (alRetValue.size() >= 2)) {
            voWh = (WhVO)alRetValue.get(0);

            freshPlanprice((ArrayList)alRetValue.get(1));
          }
        } else {
          voWh = (WhVO)oRet;
        }
        BillItem biCalBody = getBillCardPanel().getHeadItem(
          "pk_calbody");

        if (biCalBody != null) {
          if (voWh != null)
            biCalBody.setValue(voWh.getPk_calbody());
          else
            biCalBody.setValue(null);
        }
        BillItem biCalBodyname = getBillCardPanel().getHeadItem(
          "vcalbodyname");

        if (biCalBodyname != null) {
          if (voWh != null)
            biCalBodyname.setValue(voWh.getVcalbodyname());
          else {
            biCalBodyname.setValue(null);
          }
        }
        if (this.m_voBill != null) {
          this.m_voBill.setWh(voWh);

          this.m_voBill.clearInvQtyInfo();
          getLotNumbRefPane().setWHParams(voWh);
        }

        setBtnStatusSpace(true);
      }
    } catch (Exception e2) {
      SCMEnv.out(e2);
    }
  }

  public boolean beforeBillItemEdit(BillEditEvent e) {
    return true;
  }

  protected void beforeBillItemSelChg(int iRow, int iCol) {
  }

  protected boolean checkVO(GeneralBillVO voBill) {
    String sSourceBillType = getSourBillTypeCode();
    try {
      boolean bCheck = true;
      bCheck = checkVO();

      if ((sSourceBillType != null) && (
        (sSourceBillType.equals(BillTypeConst.m_assembly)) || 
        (sSourceBillType
        .equals(BillTypeConst.m_disassembly)) || 
        (sSourceBillType
        .equals(BillTypeConst.m_transform)) || 
        (sSourceBillType.equals(BillTypeConst.m_check)) || 
        (sSourceBillType
        .equals(BillTypeConst.m_AllocationOrder)))) {
        VOCheck.checkGreaterThanZeroInput(
          voBill.getChildrenVO(), 
          this.m_sNumItemKey, 
          NCLangRes.getInstance().getStrByID("common", 
          "UC000-0002282"));
      }

      return bCheck;
    } catch (ICPriceException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(
        getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      return false;
    } catch (ValidationException e) {
      SCMEnv.out("校验异常！其他未知故障...");
      handleException(e);
    }
    return false;
  }

  public void filterWhRef(String sPk_calbody) {
    String[] sConstraint = (String[])null;
    if (sPk_calbody != null) {
      sConstraint = new String[1];
      sConstraint[0] = (" AND pk_calbody='" + sPk_calbody + "'");
    }
    BillItem bi = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey);
    RefFilter.filtWh(bi, this.m_sCorpID, sConstraint);
  }

  public BillCardPanel getBillCardPanel() {
    return super.getBillCardPanel();
  }

  public BillListPanel getBillListPanel() {
    return super.getBillListPanel();
  }

  public int getCardTableRowNum() {
    return getBillCardPanel().getRowCount();
  }

  public GeneralBillVO getMVOBill() {
    return this.m_voBill;
  }

  public int getLastSelListHeadRow() {
    return this.m_iLastSelListHeadRow;
  }

  public ArrayList getSerialData() {
    return this.m_alSerialData;
  }

  public ArrayList getLocatorData() {
    return this.m_alLocatorData;
  }

  public String getTitle() {
    return super.getTitle();
  }

  public UITextField getUITxtFldStatus() {
    return this.m_uitfHinttext;
  }

  public void initialize() {
    super.initialize();

    BillItem bi = getBillCardPanel().getHeadItem("cotherwhid");

    RefFilter.filtWh(bi, this.m_sCorpID, null);
  }

  protected void initPanel() {
    super.setBillInOutFlag(1);

    super.setNeedBillRef(false);

    this.m_sBillTypeCode = BillTypeConst.m_otherIn;

    this.m_sCurrentBillNode = "40080608";
  }

  public void onButtonClicked(ButtonObject bo) {
    if (bo == getBoQueryPrice())
      onQueryPrice();
       
	  //add by yqq 2017-05-03  增加其他入库单的EXCEL导入按钮
//	else if (bo == getButtonTree().getButton(ICButtonConst.BTN_REF_EXCEL)) onEXCEL();   //EXCEL导入
//    else if (bo == getBoExcel()) 
//    	onExcel();   
    
    else
      super.onButtonClicked(bo);
  } 
  
  //edit by yqq 2017-05-03
//  private ButtonObject getBoExcel() {
//	    return getButtonTree().getButton("EXCEL导入");
//	  } 
//  
//	private int res;  
//	private nc.ui.pub.beans.UITextField txtfFileUrl = null;// 文本框,用于显示文件路径
//	private File txtFile = null;
//  
//	@SuppressWarnings("static-access")
//	private void onExcel() {
//	    UIFileChooser fileChooser = new UIFileChooser();
//	    fileChooser.setAcceptAllFileFilterUsed(true);
//	    this.res = fileChooser.showOpenDialog(this);
//		if (res == 0) {
//			getTFLocalFile().setText(
//					fileChooser.getSelectedFile().getAbsolutePath());
//			txtFile = fileChooser.getSelectedFile();
//			String filepath = txtFile.getAbsolutePath();
//			final WriteToExcel exceldata = new WriteToExcel();
//			exceldata.creatFile(filepath);
//			Runnable checkRun = new Runnable() {
//				@SuppressWarnings( { "unchecked" })
//				public void run() {
//					BannerDialog dialog = new BannerDialog(getParent());
//					dialog.start();
//					try {
//						exceldata.readData(0);
//						GeneralBillVO[] vos = WriteToExcel.generalbillvo;
//						HashMap<String, GeneralBillVO> map = new HashMap<String, GeneralBillVO>(); // 相同单据号
//
//						StringBuffer info = new StringBuffer("");
//						for (int i = 0; i < vos.length; i++) {
//							GeneralBillHeaderVO hvo = vos[i].getHeaderVO();
//							String err = hvo.getVuserdef9() == null ? "" : hvo.getVuserdef9().toString();
//							if (err.trim().length() > 0) {
//								info
//										.append("第(" + (i + 3) + ")行字段：" + err
//												+ "");
//							}
//						}
//						if (!info.toString().trim().equals("")) {
//							showErrorMessage(info.toString()
//									+ "请核实上述部分字段是否存在或分配至当前公司!");
//							showHintMessage("其他入库导入失败！");
//						} else {
//							for (int i = 0; i < vos.length; i++) {
//								GeneralBillVO voi = vos[i];
//								String code = voi.getHeaderVO().getVbillcode();
//
//								if (!map.containsKey(code)) {
//									map.put(code, voi);
//								} else {
//									GeneralBillItemVO[] bvo = (GeneralBillItemVO[]) voi
//											.getChildrenVO();
//									GeneralBillVO aggvo = map.get(code);
//									GeneralBillItemVO[] aggbvo = (GeneralBillItemVO[]) aggvo
//											.getChildrenVO();
//									GeneralBillItemVO[] newvo = new GeneralBillItemVO[bvo.length
//											+ aggbvo.length];
//									for (int j = 0; j < bvo.length; j++) {
//										bvo[j].setCrowno(String.valueOf(((j + 1) * 10)));
//										newvo[j] = bvo[j];
//									}
//									for (int j = 0; j < aggbvo.length; j++) {
//										aggbvo[j].setCrowno(String.valueOf(((j+ bvo.length + 1) * 10)));
//										newvo[bvo.length + j] = aggbvo[j];
//									}
//									map.get(code).setChildrenVO(newvo);
//								}
//							}
//							Iterator it = map.entrySet().iterator();
//							List<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
//							while (it.hasNext()) {
//								Map.Entry entry = (Map.Entry) it.next();
//								GeneralBillVO value = (GeneralBillVO) entry.getValue();
//								list.add(value);
//							}
//							
//
//							IPubDMO ipubdmo = (IPubDMO) NCLocator.getInstance()
//									.lookup(IPubDMO.class.getName());
//							ipubdmo.N_XX_Action("SAVE", "4A",getClientEnvironment().getDate().toString(), list);
//
//							showHintMessage("其他入库导入成功！");
//  						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						showErrorMessage(e.getMessage());
//						showHintMessage("其他入库导入失败！");
//					} finally {
//						dialog.end();
//
//					}
//				}
//			};
//			new Thread(checkRun).start();
//		} else {
//			return;
//		}
//    }
//
//	private nc.ui.pub.beans.UITextField getTFLocalFile() {
//		if (txtfFileUrl == null) {
//			try {
//				txtfFileUrl = new nc.ui.pub.beans.UITextField();
//				txtfFileUrl.setName("txtfFileUrl");
//				txtfFileUrl.setBounds(270, 160, 230, 26);
//				txtfFileUrl.setMaxLength(2000);
//				txtfFileUrl.setEditable(false);
//
//			} catch (java.lang.Throwable e) {
//				handleException(e);
//			}
//		}
//		return txtfFileUrl;
//	}  
  
  //end by yqq 2017-05-03
  

  private void onQueryPrice() {
    try {
      GeneralBillVO voCurBill = getBillVO();
      if ((voCurBill != null) && (voCurBill.getHeaderVO() != null) && 
        (voCurBill.getItemVOs() != null) && 
        (voCurBill.getItemVOs().length > 0)) {
        GeneralBillHeaderVO voHead = voCurBill.getHeaderVO();
        GeneralBillItemVO[] voaItem = voCurBill.getItemVOs();

        String pk_calbody = voHead.getPk_calbody();
        String cwarehouseid = voHead.getCwarehouseid();
        Hashtable htInvBatch = new Hashtable();
        if ((cwarehouseid != null) && 
          (cwarehouseid.trim().length() > 0)) {
          String[] cinventoryids = (String[])null;
          Vector vInv = new Vector();
          String[] sLots = (String[])null;
          Vector vLot = new Vector();
          String sKey = null;
          for (int i = 0; i < voaItem.length; i++) {
            if ((voaItem[i] == null) || 
              (voaItem[i].getCinventoryid() == null)) continue;
            sKey = voaItem[i].getCinventoryid() + "&" + 
              voaItem[i].getVbatchcode();
            if (!htInvBatch.containsKey(sKey)) {
              vInv.addElement(voaItem[i].getCinventoryid());

              vLot.addElement(voaItem[i].getVbatchcode());
              htInvBatch.put(sKey, "");
            }

          }

          if (vInv.size() > 0) {
            cinventoryids = new String[vInv.size()];
            vInv.copyInto(cinventoryids);
            sLots = new String[vLot.size()];
            vLot.copyInto(sLots);

            ArrayList alParam = new ArrayList();
            alParam.add(this.m_sCorpID);
            alParam.add(pk_calbody);
            alParam.add(cwarehouseid);
            alParam.add(cinventoryids);
            alParam.add(sLots);

            ArrayList alRet = (ArrayList)ICReportHelper.queryInfo(
              new Integer(19), alParam);

            if ((alRet != null) && (alRet.size() > 0)) {
              setPrice(alRet);
              showHintMessage(NCLangRes.getInstance().getStrByID(
                "4008busi", "UPP4008busi-000285"));
            } else {
              showWarningMessage(NCLangRes.getInstance()
                .getStrByID("4008busi", 
                "UPP4008busi-000286"));
            }
          } else {
            showHintMessage(NCLangRes.getInstance().getStrByID(
              "4008busi", "UPP4008busi-000287"));
          }
        } else {
          showHintMessage(NCLangRes.getInstance().getStrByID(
            "4008busi", "UPP4008busi-000288"));
        }
      } else {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", 
          "UPP4008busi-000289"));
      }
    } catch (Exception e) {
      showWarningMessage(e.getMessage());
    }
  }

  public void queryBills(QryConditionVO voQryCond) {
    try {
      this.m_alListData = GeneralBillHelper.queryBills(
        this.m_sBillTypeCode, voQryCond);
      if ((this.m_alListData != null) && (this.m_alListData.size() > 0)) {
        GeneralBillHeaderVO[] voh = new GeneralBillHeaderVO[this.m_alListData
          .size()];
        for (int i = 0; i < this.m_alListData.size(); i++) {
          if (this.m_alListData.get(i) != null)
            voh[i] = 
              ((GeneralBillHeaderVO)((GeneralBillVO)this.m_alListData
              .get(i)).getParentVO());
          else {
            SCMEnv.out("list data error!-->" + i);
          }
        }
        setListHeadData(voh);

        if (5 == this.m_iCurPanel) {
          onSwitch();
        }

        selectListBill(0);

        this.m_iLastSelListHeadRow = 0;

        this.m_iCurDispBillNum = -1;

        this.m_iBillQty = this.m_alListData.size();

        setButtonStatus(true);
        if (this.m_iBillQty > 0)
          showHintMessage(NCLangRes.getInstance().getStrByID(
            "4008busi", "UPP4008busi-000290", null, 
            new String[] { String.valueOf(this.m_iBillQty) }));
        else
          showHintMessage(NCLangRes.getInstance().getStrByID(
            "4008busi", "UPP4008busi-000291"));
      }
    }
    catch (Exception e)
    {
      handleException(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", 
        "UPP4008busi-000292"));
    }
  }

  public void removeListHeadMouseListener() {
  }

  protected void selectBillOnListPanel(int iBillIndex) {
  }

  public void setAllData(ArrayList alListData) {
    try {
      this.m_bIsOutBill = true;
      this.m_alListData = alListData;
      if ((this.m_alListData != null) && (this.m_alListData.size() > 0)) {
        GeneralBillHeaderVO[] voh = new GeneralBillHeaderVO[this.m_alListData
          .size()];
        for (int i = 0; i < this.m_alListData.size(); i++) {
          if (this.m_alListData.get(i) != null)
            voh[i] = 
              ((GeneralBillHeaderVO)((GeneralBillVO)this.m_alListData
              .get(i)).getParentVO());
          else {
            SCMEnv.out("list data error!-->" + i);
          }
        }
        setListHeadData(voh);

        selectListBill(0);

        this.m_iLastSelListHeadRow = 0;

        this.m_iCurDispBillNum = -1;

        this.m_iBillQty = this.m_alListData.size();

        if ((this.m_iLastSelListHeadRow >= 0) && 
          (this.m_iCurDispBillNum != this.m_iLastSelListHeadRow) && 
          (this.m_alListData != null) && 
          (this.m_alListData.size() > this.m_iLastSelListHeadRow) && 
          (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
          for (int i = 0; i < this.m_alListData.size(); i++) {
            this.m_voBill = 
              ((GeneralBillVO)this.m_alListData
              .get(i));

            if (i != 0)
              setBillVO(this.m_voBill);
            afterWhEditNoClearCalbody(null);
            this.m_alListData.set(i, this.m_voBill);
          }

          this.m_voBill = 
            ((GeneralBillVO)this.m_alListData
            .get(this.m_iLastSelListHeadRow));

          setBillVO(this.m_voBill);
        }

        setButtonStatus(true);
      }
    }
    catch (Exception e) {
      handleException(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", 
        "UPP4008busi-000292"));
    }
  }

  public boolean setBillCodeAuto() {
    BillItem bi = getBillCardPanel().getHeadItem("vbillcode");
    if ((bi != null) && 
      ((bi.getValue() == null) || (bi.getValue().trim().length() == 0)) && 
      (!this.m_bIsEditBillCode)) {
      this.m_voBill.setVBillCode(this.m_sCorpID);
      bi.setValue(this.m_sCorpID);
    }

    return true;
  }

  protected void setBillValueVO(GeneralBillVO bvo) {
    getBillCardPanel().getBillModel().removeTableModelListener(this);
    try {
      getBillCardPanel().setBillValueVO(bvo);

      getBillCardPanel().getBillModel().execLoadFormula();
      execHeadTailFormulas();

      bvo.clearInvQtyInfo();

      getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    getBillCardPanel().getBillModel().addTableModelListener(this);

    ctrlSourceBillUI(true);
  }

  public void setBillVO(GeneralBillVO bvo, boolean bIsOnlySet) {
    try {
      if (bIsOnlySet) {
        getBillCardPanel().addNew();
        setBillValueVO(bvo);
        this.m_voBill = bvo;
      } else {
        setBillVO(bvo);
      }

      getBillCardPanel().getBillModel().execLoadFormula();
      execHeadTailFormulas();
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public void setBodyMenuShow(boolean bShowFlag) {
    getBillCardPanel().setBodyMenuShow(bShowFlag);
  }

  protected void setButtonsStatus(int iBillMode) {
    switch (iBillMode) {
    case 0:
    case 2:
      getBoQueryPrice().setEnabled(true);
      break;
    case 3:
      getBoQueryPrice().setEnabled(false);
    case 1:
    }
    updateButton(getBoQueryPrice());
  }

  public void setCardMode(int NewCardMode) {
    this.m_iMode = NewCardMode;
  }

  public void setCardPanelEnable(boolean bEnable) {
    getBillCardPanel().setEnabled(bEnable);
  }

  public void setHeadItemEnable(String sItemKey, boolean bCan) {
    BillItem bi = getBillCardPanel().getHeadItem(sItemKey);
    if (bi != null)
      bi.setEnabled(bCan);
  }

  public void setLastSelListHeadRow(int lastrow) {
    this.m_iLastSelListHeadRow = lastrow;
    selectListBill(lastrow);
  }

  private void setPrice(ArrayList alData) {
    String IK_INV = "cinventoryid";
    String IK_BATCH_CODE = "vbatchcode";
    String IK_PRICE = "nprice";

    if ((alData != null) && (alData.size() > 0)) {
      getBillCardPanel().getBillModel().setNeedCalculate(false);

      Hashtable htData = new Hashtable();
      ArrayList alTempData = null;
      String sKey = null;
      for (int i = 0; i < alData.size(); i++) {
        if (alData.get(i) != null) {
          alTempData = (ArrayList)alData.get(i);
          if ((alTempData.size() < 3) || (alTempData.get(0) == null) || 
            (alTempData.get(2) == null)) {
            continue;
          }
          sKey = alTempData.get(0).toString() + "&" + 
            alTempData.get(1);
          if (!htData.containsKey(sKey)) {
            htData.put(sKey, alTempData.get(2));
          }
        }
      }
      int rowcount = getBillCardPanel().getRowCount();
      String cinventoryid = null;
      String vlot = null;
      for (int i = 0; i < rowcount; i++) {
        cinventoryid = (String)getBillCardPanel().getBodyValueAt(i, 
          "cinventoryid");
        vlot = (String)getBillCardPanel().getBodyValueAt(i, 
          "vbatchcode");

        sKey = cinventoryid + "&" + vlot;
        if ((sKey != null) && (htData.containsKey(sKey))) {
          getBillCardPanel().setBodyValueAt(htData.get(sKey), i, 
            "nprice");
        }
      }

      getBillCardPanel().getBillModel().setNeedCalculate(true);
      getBillCardPanel().getBillModel().reCalcurateAll();
    } else {
      SCMEnv.out("no price to be set");
    }
  }

  public void setSerialData(ArrayList alSN) {
    this.m_alSerialData = alSN;
  }

  public void setUITxtFldStatus(UITextField hinttext) {
    this.m_uitfHinttext = hinttext;
  }

  public void showErrorMessage(String sMessage) {
    MessageDialog.showErrorDlg(
      this, 
      NCLangRes.getInstance().getStrByID("4008busi", 
      "UPPSCMCommon-000059"), sMessage);
  }

  public void showHintMessage(String sMessage) {
    if (this.m_bIsOutBill)
      getUITxtFldStatus().setText(sMessage);
    else
      super.showHintMessage(sMessage);
  }

  private ButtonObject getBoQueryPrice() {
    return getButtonTree().getButton("取结存单价");
  }
  

  public void setSpace(String cspaceid, String vspacename, int i)
  {
    getBillCardPanel().setBodyValueAt(cspaceid, i, "cspaceid");
    getBillCardPanel().setBodyValueAt(vspacename, i, "vspacename");
    LocatorVO voSpace = new LocatorVO();
    LocatorVO[] lvos = new LocatorVO[1];
    lvos[0] = voSpace;
    voSpace.setCspaceid(cspaceid);
    voSpace.setVspacename(vspacename);

    this.m_alLocatorData.remove(i);
    this.m_alLocatorData.add(i, lvos);
    UFDouble assistnum = null;
    try {
      assistnum = (UFDouble)getBillCardPanel().getBodyValueAt(i, 
        this.m_sAstItemKey);
    } catch (Exception localException) {
    }
    UFDouble num = null;
    try {
      num = (UFDouble)getBillCardPanel()
        .getBodyValueAt(i, this.m_sNumItemKey);
    } catch (Exception localException1) {
    }
    UFDouble ngrossnum = null;
    try {
      ngrossnum = (UFDouble)getBillCardPanel().getBodyValueAt(i, 
        this.m_sNgrossnum);
    }
    catch (Exception localException2) {
    }
    LocatorVO[] voLoc = (LocatorVO[])this.m_alLocatorData.get(i);

    if ((voLoc != null) && (voLoc.length == 1))
    {
      if (assistnum == null) {
        voLoc[0].setNinspaceassistnum(assistnum);
        voLoc[0].setNoutspaceassistnum(assistnum);
      }
      else if (this.m_iBillInOutFlag > 0) {
        voLoc[0].setNinspaceassistnum(assistnum);
        voLoc[0].setNoutspaceassistnum(null);
      } else {
        voLoc[0].setNinspaceassistnum(null);
        voLoc[0].setNoutspaceassistnum(assistnum);
      }

      if (num == null) {
        voLoc[0].setNinspacenum(num);
        voLoc[0].setNoutspacenum(num);
        if (this.m_alSerialData != null)
          this.m_alSerialData.set(i, null);
      }
      else if (this.m_iBillInOutFlag > 0)
      {
        voLoc[0].setNoutspacenum(null);
        voLoc[0].setNinspacenum(num);
      }
      else {
        voLoc[0].setNinspacenum(null);
        voLoc[0].setNoutspacenum(num);
        if (this.m_alSerialData != null) {
          this.m_alSerialData.set(i, null);
        }
      }

      if (ngrossnum == null) {
        voLoc[0].setNingrossnum(ngrossnum);
        voLoc[0].setNoutgrossnum(ngrossnum);
        if (this.m_alSerialData != null)
          this.m_alSerialData.set(i, null);
      }
      else if (this.m_iBillInOutFlag > 0)
      {
        voLoc[0].setNoutgrossnum(null);
        voLoc[0].setNingrossnum(ngrossnum);
      }
      else {
        voLoc[0].setNingrossnum(null);
        voLoc[0].setNoutgrossnum(ngrossnum);
      }

    }
    else
    {
      this.m_alLocatorData.set(i, null);
    }
  }

  public String getParentCorpCode()
  {
    String ParentCorp = new String();
    String key = ClientEnvironment.getInstance().getCorporation()
      .getFathercorp();
    try {
      CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
      ParentCorp = corpVO.getUnitcode();
    }
    catch (BusinessException e) {
      e.printStackTrace();
    }
    return ParentCorp;
  }

  public boolean Iscsflag(String primkey) {
    boolean rst = false;
    String SQL = "select csflag from bd_stordoc  where pk_stordoc ='" + primkey + "'";
    IUAPQueryBS sessionManager = (IUAPQueryBS)
      NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    List list = null;
    try {
      list = (List)sessionManager.executeQuery(SQL, 
        new ArrayListProcessor());

      if (list.isEmpty()) {
        return rst;
      }

      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
        ArrayList values = new ArrayList();
        Object obj = iterator.next();
        if (obj == null) {
          continue;
        }
        if (obj.getClass().isArray()) {
          int len = Array.getLength(obj);
          for (int j = 0; j < len; j++) {
            values.add(Array.get(obj, j));
          }
          return rst = new UFBoolean(String.valueOf(values.get(0))).booleanValue();
        }

      }

    }
    catch (BusinessException e)
    {
      e.printStackTrace();
    }

    return rst;
  }
}