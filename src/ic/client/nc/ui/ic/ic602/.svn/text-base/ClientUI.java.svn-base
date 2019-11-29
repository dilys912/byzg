package nc.ui.ic.ic602;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.query.EditComponentFacotry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.dm104.DelivbillHVO;
import nc.vo.ic.ic602.ForecastinvHeadVO;
import nc.vo.ic.ic602.ForecastinvVO;
import nc.vo.ic.ic602.ForecastinvbillVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 库存展望量查询UI界面 创建日期：(2001-7-24 15:05:21)
 * 
 * @author：张欣
 */

public class ClientUI extends nc.ui.ic.pub.report.IcBaseReport {
    /** 报表模板界面类 */
    private ReportBaseClass ivjReportBase = null;

    /** 查询条件对话框 */
    private ICMultiCorpQryClient ivjQueryConditionDlg = null;

    /** 报表模板节点名 */
    private String m_sRNodeName = "40083004SYS";

 


    /** 是否按自由项展开 */
    private boolean m_bisFreeExtend = false;

    /** 是否按自由项展开 */
    private boolean m_bisBatchExtend = false;

    /** 是否按自由项展开 */
    private boolean m_bisWhExtend = false;

    //
    private ArrayList m_alAllData = null;

    /**
     * to store the every inventory 's forecast inv qty, the sequence is exactly
     * to the inv sequence
     */
    /** [[UFDouble[]],[UFDouble[]],[UFDouble[]],[UFDouble[]]] */
    //
    private String m_sForeDays = null;

    private String m_sCorpID = null; //公司ID

    private String m_sCorpCode = null; //Cooperation code

    private String m_sCorpName = null; //Cooperation name

    private String m_sUserID = null; //当前使用者ID

    private String m_sLogDate = null; //当前登录日期

    private String m_sUserName = null; //当前使用者名称

    private String m_sUserCode = null; //usercode

    //

    /** 界面按钮 */
    private ButtonObject m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UC001-0000006")/* @res "查询" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                    "UC001-0000006")/* @res "查询" */, 2, "查询"); /*-=notranslate=-*/

    private ButtonObject m_boDateExtend = new ButtonObject(
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40083004",
                    "UPT40083004-000029")/* @res "按日期展开" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40083004",
                    "UPT40083004-000029")/* @res "按日期展开" */, 2, "按日期展开"); /*-=notranslate=-*/

    /** 界面显示按钮组 */
    private ButtonObject[] m_MainButtonGroup = { m_boQuery, m_boDateExtend };

    private ExtendDateDlg ivjExtendDateDlg1 = null;

    private String[] m_sReportItemKey = null;

    /**
     * ClientUI 构造子注解。
     */
    public ClientUI() {
        super();
        initialize();
    }

    /**
     * 此方法应在查询方法得到查询条件之后调用。 根据用户定义的商业或工业（原材料，产成品）的参数，设置报表界面可视的列 功能：
     * 参数：m_iCommercialorIndustry 返回：void 例外： 日期：(2001-8-9 15:18:51)
     * 修改日期，修改人，修改原因，注释标志：
     *  
     */
    public void adjustReportUI() {
        /** 得到用户设置的定义构成预计库存展望量的各个数据项 */
        Hashtable htSysParams = null;
        try {
            htSysParams = getSysParams();
        } catch (Exception e) {
            nc.vo.scm.pub.SCMEnv.error(e);

        }

        //String[] sReportItemKey =
        //{
        //"npraynum",
        //"npurchaseordernum",
        //"naccumchecknum",
        //"nshldtraninnum",
        //"nplannedordernum",
        //"nmanufordernum",
        //"nborrownum",
        //"nfreezenum",
        //"nsaleordernum",
        //"ndelivernum",
        //"nshldtranoutnum",
        //"npreparematerialnum",
        //"nwwnum",
        //"nsafestocknum" };
        ///**
        //IC037 预计入是否包含在请购量
        //IC038 预计入是否包含在途采购订单量
        //IC039 预计入是否包含到货待检量
        //IC040 预计入是否包含转库单在途
        //IC041 预计入是否包含生产系统计划定单
        //IC042 预计入是否包含生产订单
        //IC043 借入量是否可用
        //IC044 冻结量是否可用
        //IC045 预计出是否包含销售定单承诺量
        //IC046 预计出是否包含待发货单
        //IC047 预计出是否包含转库单待转出量
        //IC048 预计出是否包含备料计划占用量
        //IC050 预计入是否包含委托加工订单数
        //IC051 可用量是否保含安全库存
        //*/

        //隐藏展望日期
        getReportBaseClass().hideColumn("foreday");

    }

    /**
     * 创建者：张欣 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
     * 修改日期，修改人，修改原因，注释标志：
     * 
     * 
     * 
     *  
     */
    protected void getCEnvInfo() {
        try {
            //当前使用者ID
            nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
                    .getInstance();
            try {
                m_sUserID = ce.getUser().getPrimaryKey();
                m_sUserCode = ce.getUser().getUserCode();
            } catch (Exception e) {

            }
            //nc.vo.scm.pub.SCMEnv.out("test user id is 2011000001");
            //m_sUserID="2011000001";
            //当前使用者姓名
            try {
                m_sUserName = ce.getUser().getUserName();
            } catch (Exception e) {

            }
            //nc.vo.scm.pub.SCMEnv.out("test user name is 张三");
            //m_sUserName="张三";
            //公司ID
            try {
                m_sCorpID = ce.getCorporation().getPrimaryKey();
                m_sCorpName = ce.getCorporation().getUnitname();
                m_sCorpCode = ce.getCorporation().getUnitcode();
                nc.vo.scm.pub.SCMEnv.out("---->corp id is " + m_sCorpID);
            } catch (Exception e) {

            }
            //日期
            try {
                if (ce.getDate() != null)
                    m_sLogDate = ce.getDate().toString();
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
    }

    /**
     * 返回 QueryConditionClient1 特性值。
     * 
     * @return nc.ui.pub.query.QueryConditionClient
     */
    private ICMultiCorpQryClient getConditionDlg() {
        if (ivjQueryConditionDlg == null) {
            ivjQueryConditionDlg = new ICMultiCorpQryClient(this, "查询", m_sUserID,
                    m_sCorpID, "40083004");
            //读查询模版数据
            ivjQueryConditionDlg.setTempletID(m_sCorpID, getPNodeCode(),
                    m_sUserID, null);

            ivjQueryConditionDlg.setAutoClear("pk_corp", new String[] {
                    "ccalbodyid", "invcode" });
            //以下为对参照的初始化
           
            ivjQueryConditionDlg.initRefWhere("inv.invcode",
            " and bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N' ");

            ivjQueryConditionDlg. setPowerRefsOfCorp("pk_corp", new String[] { "ccalbodyid", "cwarehouseid","invclasscode","invcode" ,"cinventoryid"},null);
            //eric 2012-08-16 初始化查询条件
            QueryConditionVO[] vodatas = ivjQueryConditionDlg.getConditionDatas();
            Object ovo = getGvo();
            if(ovo instanceof GeneralBillVO){
            GeneralBillVO gvo = (GeneralBillVO) ovo;
            if(gvo!=null){
            GeneralBillItemVO[] vos = gvo.getItemVOs();
            GeneralBillHeaderVO vo = gvo.getHeaderVO();
            UICheckBox ref = null;
            for (int i = 0; i < vodatas.length; i++) {
            	if(vodatas[i].getFieldCode().equals("invcode")){
            		String invcodes = "";
            		for(int j=0 ; j<vos.length ; j++){
            			if(j==0)
            				invcodes = vos[j].getCinventorycode();
            			else 
            				invcodes = invcodes.concat(",").concat(vos[j].getCinventorycode());
            		}
            		vodatas[i].setValue(invcodes); 
            	}
            	if(vodatas[i].getFieldCode().equals("vbatchcode")){
            		String batchcode = "";
            		for(int j=0 ; j<vos.length ; j++){
            			if(vos[j].getVbatchcode()!=null){
                			if(batchcode.equals(""))
                				batchcode = vos[j].getVbatchcode();
                			else 
                				batchcode = batchcode.concat("','").concat(vos[j].getVbatchcode());
                			}
            		}
            		vodatas[i].setValue(batchcode);
            	} 
            	if(vodatas[i].getFieldCode().equals("isbatchext")){
            		vodatas[i].setIfDefault(UFBoolean.TRUE); 
            		vodatas[i].setValue(UFBoolean.TRUE.toString());
            		
            	} 
            	if(vodatas[i].getFieldCode().equals("vfree0")){
            		vodatas[i].setIfDefault(UFBoolean.TRUE); 
            		vodatas[i].setValue(UFBoolean.TRUE.toString());
//            		EditComponentFacotry factry = new EditComponentFacotry(vodatas[i]);
//    				ref = (UICheckBox) factry.getEditComponent(null);
//            		ref.setEnabled(false); 
//            		ref.setVisible(false);
//            		ref.updateUI();
            	} 
            	if(vodatas[i].getFieldCode().equals("iswhext")){
            		vodatas[i].setIfDefault(UFBoolean.TRUE); 
            		vodatas[i].setValue(UFBoolean.TRUE.toString());
            		
            	} 
            	if(vodatas[i].getFieldCode().equals("cwarehouseid")){
            		vodatas[i].setValue(vo.getCwarehousename());
            	} 
            	if(vodatas[i].getFieldCode().equals("ccalbodyid")){
            		vodatas[i].setValue(vo.getVcalbodyname());
            	} 
            }
            }
            }
            
            if(ovo instanceof DelivbillHVO){
            	DelivbillHVO gvo = (DelivbillHVO) ovo;
                if(gvo!=null){
                	DelivbillHItemVO[] vos = (DelivbillHItemVO[]) gvo.getChildrenVO();
                	DelivbillHHeaderVO vo = (DelivbillHHeaderVO) gvo.getParentVO();
            	nc.ui.pub.beans.UIRefPane ref = null;
                for (int i = 0; i < vodatas.length; i++) {
                	if(vodatas[i].getFieldCode().equals("invcode")){
                		String invcodes = "";
                		for(int j=0 ; j<vos.length ; j++){
                			if(j==0)
                				invcodes = vos[j].getVinvcode();
                			else 
                				invcodes = invcodes.concat(",").concat(vos[j].getVinvcode());
                		}
                		vodatas[i].setValue(invcodes); 
                	}
                	if(vodatas[i].getFieldCode().equals("vbatchcode")){
                		String batchcode = "";
                		for(int j=0 ; j<vos.length ; j++){
                			if(vos[j].getVbatchcode()!=null){
                    			if(batchcode.equals(""))
                    				batchcode = vos[j].getVbatchcode();
                    			else 
                    				batchcode = batchcode.concat("','").concat(vos[j].getVbatchcode());
                    			}
                		}
                		vodatas[i].setValue(batchcode);
                	} 
                	if(vodatas[i].getFieldCode().equals("isbatchext")){
                		vodatas[i].setIfDefault(UFBoolean.TRUE); 
                		vodatas[i].setValue(UFBoolean.TRUE.toString());
                	
                	} if(vodatas[i].getFieldCode().equals("vfree0")){
                		vodatas[i].setIfDefault(UFBoolean.TRUE); 
                		vodatas[i].setValue(UFBoolean.TRUE.toString());
                		
                	}  if(vodatas[i].getFieldCode().equals("iswhext")){
                		vodatas[i].setIfDefault(UFBoolean.TRUE); 
                		vodatas[i].setValue(UFBoolean.TRUE.toString());
                		
                	} 
                	if(vodatas[i].getFieldCode().equals("cwarehouseid")){
                		vodatas[i].setValue(vos[0].getVsendstorename());
                	} 
                	
                }
                }
                }
            
            
            ivjQueryConditionDlg.hideNormal(); 
        }
        return ivjQueryConditionDlg;
    }
    public Object gvo;
    

    public Object getGvo() {
		return gvo;
	}

	public void setGvo(Object gvo) {
		this.gvo = gvo;
	}

	/**
     * 返回 ExtendDateDlg1 特性值。
     * 
     * @return nc.ui.ic.ic602.ExtendDateDlg
     */
    /* 警告：此方法将重新生成。 */
    private ExtendDateDlg getExtendDateDlg1() {
        if (ivjExtendDateDlg1 == null) {
            try {
                ivjExtendDateDlg1 = new nc.ui.ic.ic602.ExtendDateDlg(this);
                ivjExtendDateDlg1.setName("ExtendDateDlg1");
                //ivjExtendDateDlg1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                // user code begin {1}
                ivjExtendDateDlg1.adjustItem();
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjExtendDateDlg1;
    }

    /**
     * 创建者：王乃军 功能：得到一个初始化的动态添加的列 参数： 返回： 例外： 日期：(2001-8-17 13:13:51)
     * 修改日期，修改人，修改原因，注释标志：
     */
    protected ReportItem getReportItem(String sKey, String sTitle) {
        ReportItem biAddItem = new ReportItem();
        biAddItem.setName(sTitle);
        biAddItem.setKey(sKey);
        biAddItem.setDataType(2);
        biAddItem.setDecimalDigits(4);
        biAddItem.setLength(80);
        biAddItem.setWidth(80);
        biAddItem.setEnabled(false);
        biAddItem.setShow(true);
        //biAddItem.setShowOrder(iStartCol + 1);
        biAddItem.setPos(1);
        return biAddItem;
    }

    /**
     * 子类实现该方法，返回业务界面的标题。
     * 
     * @version (00-6-6 13:33:25)
     * 
     * @return java.lang.String
     */
    public String getTitle() {
        if (getReportBaseClass().getReportTitle() != null)
            return getReportBaseClass().getReportTitle();
        else
            return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report",
                    "UPP4008report-000016")/* @res "库存展望量查询" */;
    }

    /**
     * 每当部件抛出异常时被调用
     * 
     * @param exception
     *            java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {

        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
        nc.vo.scm.pub.SCMEnv.error(exception);

    }

    /**
     * 此处插入方法说明。 创建日期：(2001-7-24 20:03:46)
     */
    private void initialize() {
        try {
            // user code begin {1}
            setName("ClientUI");
            setLayout(new java.awt.BorderLayout());
            setSize(774, 419);
            add(getReportBaseClass(), "Center");
            getCEnvInfo();
           // m_btnPreview.setVisible(false);
           // m_btnPrint.setVisible(false);
            //设置按钮组
            setButtons(getButtonArray(m_MainButtonGroup));

            // user code end
            setName("ClientUI");
            setSize(774, 419);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
            nc.ui.pub.beans.MessageDialog.showWarningDlg(this, null, ivjExc
                    .getMessage());
            return;
        }
        // user code begin {2}

        initReportTemplet(m_sRNodeName);
        getReportBaseClass().setRowNOShow(true);
        getReportBaseClass().setTatolRowShow(true);
        getReportBaseClass().setMaxLenOfHeadItem("corpname", 100);
        setBillHeadTailData();
        //	setCoalitionCol();
        adjustReportUI();
        //设置定义的数据精度
        nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(
                m_sUserID, m_sCorpID);
        ArrayList alTemp = new ArrayList();
        alTemp.add(new String[] { "restnum", "nfreezenum", "nborrownum",
                "nshldtraninnum", "nshldtranoutnum", "npraynum",
                "npurchaseordernum", "naccumchecknum", "nsaleordernum",
                "ndelivernum", "nforecastnum", "navailablenum",
                "nmanufordernum", "npreparematerialnum", "nplannedordernum",
                "nsafestocknum", "nwwnum", "ntranpraynum", "npreordernum" ,"ds","kyds"});//eric
        alTemp.add(null);
        alTemp.add(null);
        alTemp.add(null);
        alTemp.add(null);
        try {
            si.setScale(getReportBaseClass(), alTemp);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                showHintMessage(e.getMessage());
            } else {
                nc.vo.scm.pub.SCMEnv.error(e);
            }

        }
        addSortListener();
        // user code end
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-7-24 18:42:06)
     * 
     * @param sNodeName
     *            java.lang.String
     */
    public void initReportTemplet(String sNodeName) {

        //读取模版数据
        try {
            getReportBaseClass().setTempletID(m_sCorpID, getPNodeCode(),
                    m_sUserID, null);
        } catch (Exception e) {
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "SCMCOMMON", "UPPSCMCommon-000019")/*
                                                        * @res
                                                        * "不能得到模版，请与系统管理员联系！"
                                                        */);
            return;
        }
        nc.ui.pub.bill.BillData bd = getReportBaseClass().getBillData();
        //处理批次号档案自定义项
        bd=BatchCodeDefSetTool.changeBillDataByBCUserDef(m_sCorpID, bd);
        if (bd == null) {
            nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
            return;
        }

        getReportBaseClass().setBillData(bd);
        
        java.util.Vector vKey = new Vector();

        ReportItem[] biItems = getReportBaseClass().getBody_Items();
        if (biItems != null) {
            for (int i = 0; i < biItems.length; i++) {

                if (biItems[i] != null && biItems[i].isShow()
                        && biItems[i].getDataType() == BillItem.DECIMAL) {

                    if (!("nforecastnum".equals(biItems[i].getKey()))
                            && !("navailablenum".equals(biItems[i].getKey())))
                        vKey.addElement(biItems[i].getKey());

                }
            }
        }
        if (vKey.size() > 0) {
            m_sReportItemKey = new String[vKey.size()];
            vKey.copyInto(m_sReportItemKey);

        }

        String[] sReportItemKey = { "restnum", "npraynum", "npurchaseordernum",
                "naccumchecknum", "nshldtraninnum", "nplannedordernum",
                "nmanufordernum", "nborrownum", "nfreezenum", "nsaleordernum",
                "ndelivernum", "nshldtranoutnum", "npreparematerialnum",
                "nwwnum", "nsafestocknum", "nforecastnum", "navailablenum" };

        /**
         * IC037 预计入是否包含在请购量 IC038 预计入是否包含在途采购订单量 IC039 预计入是否包含到货待检量 IC040
         * 预计入是否包含转库单在途 IC041 预计入是否包含生产系统计划定单 IC042 预计入是否包含生产订单 IC043 借入量是否可用
         * IC044 冻结量是否可用 IC045 预计出是否包含销售定单承诺量 IC046 预计出是否包含待发货单 IC047
         * 预计出是否包含转库单待转出量 IC048 预计出是否包含备料计划占用量 IC050 预计入是否包含委托加工订单数 IC051
         * 可用量是否保含安全库存
         */

    }

    /**
     * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
        try {
            javax.swing.JFrame frame = new javax.swing.JFrame();
            ClientUI aClientUI;
            aClientUI = new ClientUI();
            frame.setContentPane(aClientUI);
            frame.setSize(aClientUI.getSize());
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                };
            });
            frame.show();
            java.awt.Insets insets = frame.getInsets();
            frame.setSize(frame.getWidth() + insets.left + insets.right, frame
                    .getHeight()
                    + insets.top + insets.bottom);
            frame.setVisible(true);
        } catch (Throwable exception) {
            nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
            nc.vo.scm.pub.SCMEnv.error(exception);
        }
    }

    /**
     * 子类实现该方法，响应按钮事件。
     * 
     * @version (00-6-1 10:32:59)
     * 
     * @param bo
     *            ButtonObject
     */
    public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
        if (bo == m_boQuery)
            onQuery(true);

        else if (bo == m_boDateExtend)
            onDateExtend();
        else
            super.onButtonClicked(bo);
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-27 18:06:37)
     */
    void onDateExtend() {
        int selrow = getReportBaseClass().getBillTable().getSelectedRow();
        if (selrow == -1 || m_alAllData == null || selrow == m_alAllData.size()
                || m_alAllData.get(selrow) == null) {
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "4008report", "UPP4008report-000017")/* @res "请选中查询结果行！" */);
            return;
        } else {
            int rownum = selrow;
//            int[] index = getReportBaseClass().getBillModel().getSortIndex();
//            if (index != null)
//                rownum = index[selrow];
            ForecastinvVO vo = (ForecastinvVO) m_alAllData.get(rownum);
            try {
                vo.m_isFreeExt = m_bisFreeExtend;
                vo.m_isBatchExt = m_bisBatchExtend;
                vo.m_isWhExt = m_bisWhExtend;
                vo = ForecastInvHelper.queryForecastInvQtyDetail(vo);
            } catch (Exception ex) {
                nc.vo.scm.pub.SCMEnv.error(ex);

            }
            m_voHead.setAttributeValue("invname", vo
                    .getAttributeValue("invname"));
            m_voHead.setAttributeValue("invcode", vo
                    .getAttributeValue("invcode"));
            String storname =  (String)vo.getAttributeValue("storname");
            String stornameui =  (String)getReportBaseClass().getBodyValueAt(rownum, "storname");
            if((storname==null || storname.trim().length()<=0) && stornameui!=null)
            	m_voHead.setAttributeValue("cwarehousename", stornameui);
            else
	            m_voHead.setAttributeValue("cwarehousename", storname);
            m_voHead.setAttributeValue("storeorgname", vo
                    .getAttributeValue("bodyname"));
            m_voHead.setAttributeValue("measname", vo
                    .getAttributeValue("measname"));
            m_voHead.setAttributeValue("vbatchcode", vo
                    .getAttributeValue("vbatchcode"));
            ForecastinvVO[] vos = construDateExtendVOs(vo);
            //getExtendDateDlg1().adjustItem();
            getExtendDateDlg1().getClientUI().getReportBaseClass()
                    .setHeadDataVO(m_voHead);
            getExtendDateDlg1().getClientUI().getReportBaseClass()
                    .setBodyDataVO(vos);
            getExtendDateDlg1().getClientUI().filterNullLine();
            getExtendDateDlg1().showModal();
            ivjExtendDateDlg1 = null;

        }

    }

    /**
     * 输出单据信息 功能： 参数： 返回： 例外： 日期：(2001-8-9 16:03:27) 修改日期，修改人，修改原因，注释标志：
     */
    void onOutput() {
    }

    /**
     * 根据相关查询条件，查询单据 功能： 参数： 返回： 例外： 日期：(2001-8-9 16:03:48) 修改日期，修改人，修改原因，注释标志：
     */
    //Eric 2012-08-16
    public void RefQuery(){
    	   m_boQuery.setEnabled(false);
           updateButtons();
    	 if (m_sForeDays == null)
             m_sForeDays = "10";
         if(!m_bEverQry){
 			getConditionDlg().showModal();
 			m_bEverQry=true;
 		}else{
 			getConditionDlg().onButtonConfig();
 		}

         if (!getConditionDlg().isCloseOK())
             return;
    	 setDlgSubTotal(null);
         String[] saCorp = null;//多公司查询
         try {
             nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
                     .getExpandVOs(getConditionDlg().getConditionVO());
             ArrayList alTranParam = new ArrayList();
             saCorp = getConditionDlg().getSelectedCorpIDs();
             if (saCorp == null || saCorp.length<0)
             {
                 showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
 						"4008report", "UPP4008report-000186")/*
 															  * @res
 															  * "没有选择公司查询条件!"
 															  */);
                 RefQuery();
                 return;
             }
             Object ovo = getGvo();
             if(ovo instanceof GeneralBillVO){
             GeneralBillVO gvo = (GeneralBillVO) ovo;
             GeneralBillItemVO[] vos = gvo.getItemVOs();
             GeneralBillHeaderVO vo = gvo.getHeaderVO();
             for(ConditionVO cvo : voCons){
            	if(cvo.getFieldCode().equals("cwarehouseid")){
            		RefResultVO rvo = new RefResultVO();
            		rvo.setRefPK(vo.getCwarehouseid());
            		rvo.setRefCode("01");
            		rvo.setRefName(vo.getCwarehousename());
            		cvo.setRefResult(rvo);
            		cvo.setValue(vo.getCwarehouseid());
            	}
            	if(cvo.getFieldCode().equals("invcode")){
            		RefResultVO rvo = new RefResultVO();
            		rvo.setRefCode(vos[0].getCinventorycode());
            		rvo.setRefPK(vos[0].getCinventoryid());
            		cvo.setRefResult(rvo);
            		String invcodes = "";
            		for(int j=0 ; j<vos.length ; j++){
            			if(j==0)
            				invcodes = vos[j].getCinventorycode();
            			else 
            				invcodes = invcodes.concat("','").concat(vos[j].getCinventorycode());
            		}
            		cvo.setValue(invcodes);
            	}
            	if(cvo.getFieldCode().equals("vbatchcode")){
            		String batchcode = "";
            		for(int j=0 ; j<vos.length ; j++){
            			if(vos[j].getVbatchcode()!=null){
                			if(batchcode.equals(""))
                				batchcode = vos[j].getVbatchcode();
                			else 
                				batchcode = batchcode.concat("','").concat(vos[j].getVbatchcode());
                			}
            		}
            		cvo.setValue(batchcode);
            	}
            	if(cvo.getFieldCode().equals("ccalbodyid")){
            		cvo.setValue(vo.getPk_calbody());
            	}
             }
             }
             
             if(ovo instanceof DelivbillHVO){
            	 DelivbillHVO gvo = (DelivbillHVO) ovo;
            	 DelivbillHItemVO[] vos = (DelivbillHItemVO[]) gvo.getChildrenVO();
             	DelivbillHHeaderVO vo = (DelivbillHHeaderVO) gvo.getParentVO();
                 for(ConditionVO cvo : voCons){
                	if(cvo.getFieldCode().equals("cwarehouseid")){ 
                		cvo.setValue(vos[0].getPksendstock());
                	}
                	if(cvo.getFieldCode().equals("invcode")){
                		String invcodes = "";
                		for(int j=0 ; j<vos.length ; j++){
                			if(j==0)
                				invcodes = vos[j].getVinvcode();
                			else 
                				invcodes = invcodes.concat("','").concat(vos[j].getVinvcode());
                		}
                		cvo.setValue(invcodes);
                	}
                	if(cvo.getFieldCode().equals("vbatchcode")){
                		String batchcode = "";
                		for(int j=0 ; j<vos.length ; j++){ 
                			if(vos[j].getVbatchcode()!=null){
                			if(batchcode.equals(""))
                				batchcode = vos[j].getVbatchcode();
                			else 
                				batchcode = batchcode.concat("','").concat(vos[j].getVbatchcode());
                			}
                		}
                		cvo.setValue(batchcode);
                	}
                 }
                 }
             
             
             alTranParam.add(saCorp);
             alTranParam.add(voCons);
             alTranParam.add(m_sLogDate);
             alTranParam.add(m_sForeDays);
             try {
                 m_alAllData = ForecastInvHelper.queryForecastInvQty(alTranParam);
                 nc.vo.scm.pub.SCMEnv.out(getConditionDlg().getWhereSQL());
             } catch (Exception e) {
                 nc.vo.scm.pub.SCMEnv.error(e);
             }

             ForecastinvVO[] aryforecastvos = null;
             if (m_alAllData != null && m_alAllData.size() > 0) {
                 aryforecastvos = new ForecastinvVO[m_alAllData.size()];
                 m_alAllData.toArray(aryforecastvos);
             }
             if (voCons != null && voCons.length > 0) {
                 m_voHead = new ForecastinvHeadVO();
                 m_voHead.setAttributeValue("querydate", m_sLogDate);
                 m_voHead.setAttributeValue("forecastdate", m_sForeDays);
                 for (int i = 0; i < voCons.length; i++) {
                     if (voCons[i].getFieldCode().equals("pk_corp")) {
                         m_voHead.setAttributeValue("pk_corp", voCons[i]
                                 .getRefResult().getRefPK());
                         m_voHead.setAttributeValue("corpcode", voCons[i]
                                 .getRefResult().getRefCode());
                         m_voHead.setAttributeValue("corpname", voCons[i]
                                 .getRefResult().getRefName());
                     }
                     if (voCons[i].getFieldCode().equals("invcodefrom")) {
                         m_voHead.setAttributeValue("invcode", voCons[i]
                                 .getValue());
                         m_voHead.setAttributeValue("invname", voCons[i]
                                 .getRefResult().getRefName());
                     }
                     if (voCons[i].getFieldCode().equals("invcodeto")) {
                         m_voHead.setAttributeValue("invcode", voCons[i]
                                 .getValue());
                         m_voHead.setAttributeValue("invname", voCons[i]
                                 .getRefResult().getRefName());
                     }
                     if (voCons[i].getFieldCode().equals("ccalbodyid")) {
                         if (voCons[i].getRefResult() == null)
                             continue;
                         m_voHead.setAttributeValue("storeorgname", voCons[i]
                                 .getRefResult().getRefName());
                         m_voHead.setAttributeValue("storeorgcode", voCons[i]
                                 .getRefResult().getRefCode());
                     }

                     if (voCons[i].getFieldCode().equals("vfree0")
                             && voCons[i].getValue().equals("Y")) {
                         getReportBaseClass().showHiddenColumn("vfree0");
                         m_bisFreeExtend = true;
                     } else if (voCons[i].getFieldCode().equals("vfree0")
                             && voCons[i].getValue().equals("N")) {
                         getReportBaseClass().hideColumn("vfree0");
                         m_bisFreeExtend = false;
                     }
                     if (voCons[i].getFieldCode().equals("isbatchext")
                             && voCons[i].getValue().equals("Y")) {
                         getReportBaseClass().showHiddenColumn("vbatchcode");
                         m_bisBatchExtend = true;
                     } else if (voCons[i].getFieldCode().equals("isbatchext")
                             && voCons[i].getValue().equals("N")) {
                         getReportBaseClass().hideColumn("vbatchcode");
                         m_bisBatchExtend = false;
                     }
                     if (voCons[i].getFieldCode().equals("iswhext")
                             && voCons[i].getValue().equals("Y")) {
                         getReportBaseClass().showHiddenColumn("storname");
                         m_bisWhExtend = true;
                     } else if (voCons[i].getFieldCode().equals("iswhext")
                             && voCons[i].getValue().equals("N")) {
                         getReportBaseClass().hideColumn("storname");
                         m_bisWhExtend = false;
                     }
                 }

             }
             //清空提示信息。
             showHintMessage("");
             getReportBaseClass().setHeadDataVO(m_voHead);
             if (aryforecastvos == null || aryforecastvos.length == 0) {
                 aryforecastvos = new ForecastinvVO[] { new ForecastinvVO() };
                 m_alAllData = null;
                 showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                         "4008report", "UPP4008report-000018")/*
                                                               * @res
                                                               * "没有查询到符合条件的结果。"
                                                               */);
                 nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("Clear report result!");
                 getReportBaseClass().getBillModel().clearBodyData();

             }

             //合计行
             if (m_alAllData != null && !m_alAllData.isEmpty()) {
                 ForecastinvVO fvo = (ForecastinvVO) m_alAllData.get(0);

             }
             //getReportBaseClass().setBodyDataVO(aryforecastvos);
             setReportData(aryforecastvos);
             if (m_alAllData != null && m_alAllData.size() > 0) {
                 ForecastinvVO fvo = (ForecastinvVO) m_alAllData.get(0);

                 if (fvo.getAttributeValue("nsafestocknum") != null) {
                     int columnnum = getReportBaseClass().getBodyColByKey(
                             "nsafestocknum");
                     if (getReportBaseClass().getBody_Items() != null
                             && columnnum >= 0
                             && columnnum <= getReportBaseClass()
                                     .getBody_Items().length) {
                     }
                 }
             }

             calculateTotal();

             filterNullLine();

         } catch (Exception e) {
             nc.vo.scm.pub.SCMEnv.error(e);
             showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                     "4008report", "UPP4008report-000004")/* @res "查询出错：" */
                     + e.getMessage());
         }

      
         return;
    }
    public void onQuery(boolean bQuery) {

        if (m_sForeDays == null)
            m_sForeDays = "10";
        if(bQuery||!m_bEverQry){
			getConditionDlg().showModal();
			m_bEverQry=true;
		}else{
			getConditionDlg().onButtonConfig();
		}

        if (!getConditionDlg().isCloseOK())
            return;
        setDlgSubTotal(null);
        String[] saCorp = null;//多公司查询
        try {
            nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
                    .getExpandVOs(getConditionDlg().getConditionVO());
            ArrayList alTranParam = new ArrayList();
            //alTranParam.add(m_sCorpID);
            saCorp = getConditionDlg().getSelectedCorpIDs();
            if (saCorp == null || saCorp.length<0)
            {
                showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008report", "UPP4008report-000186")/*
															  * @res
															  * "没有选择公司查询条件!"
															  */);
                onQuery(true);
                return;
            }
            alTranParam.add(saCorp);
            alTranParam.add(voCons);
            alTranParam.add(m_sLogDate);
            alTranParam.add(m_sForeDays);
            try {
                m_alAllData = ForecastInvHelper
                        .queryForecastInvQty(alTranParam);
                nc.vo.scm.pub.SCMEnv.out(getConditionDlg().getWhereSQL());
            } catch (Exception e) {
                nc.vo.scm.pub.SCMEnv.error(e);
            }

            ForecastinvVO[] aryforecastvos = null;
            if (m_alAllData != null && m_alAllData.size() > 0) {
                aryforecastvos = new ForecastinvVO[m_alAllData.size()];
                m_alAllData.toArray(aryforecastvos);
            }
            if (voCons != null && voCons.length > 0) {
                m_voHead = new ForecastinvHeadVO();
                m_voHead.setAttributeValue("querydate", m_sLogDate);
                m_voHead.setAttributeValue("forecastdate", m_sForeDays);
                for (int i = 0; i < voCons.length; i++) {
                    if (voCons[i].getFieldCode().equals("pk_corp")) {
                        m_voHead.setAttributeValue("pk_corp", voCons[i]
                                .getRefResult().getRefPK());
                        m_voHead.setAttributeValue("corpcode", voCons[i]
                                .getRefResult().getRefCode());
                        m_voHead.setAttributeValue("corpname", voCons[i]
                                .getRefResult().getRefName());
                    }
                    if (voCons[i].getFieldCode().equals("invcodefrom")) {
                        m_voHead.setAttributeValue("invcode", voCons[i]
                                .getValue());
                        m_voHead.setAttributeValue("invname", voCons[i]
                                .getRefResult().getRefName());
                    }
                    if (voCons[i].getFieldCode().equals("invcodeto")) {
                        m_voHead.setAttributeValue("invcode", voCons[i]
                                .getValue());
                        m_voHead.setAttributeValue("invname", voCons[i]
                                .getRefResult().getRefName());
                    }
                    if (voCons[i].getFieldCode().equals("ccalbodyid")) {
                        if (voCons[i].getRefResult() == null)
                            continue;
                        m_voHead.setAttributeValue("storeorgname", voCons[i]
                                .getRefResult().getRefName());
                        m_voHead.setAttributeValue("storeorgcode", voCons[i]
                                .getRefResult().getRefCode());
                    }

                    if (voCons[i].getFieldCode().equals("vfree0")
                            && voCons[i].getValue().equals("Y")) {
                        getReportBaseClass().showHiddenColumn("vfree0");
                        m_bisFreeExtend = true;
                    } else if (voCons[i].getFieldCode().equals("vfree0")
                            && voCons[i].getValue().equals("N")) {
                        getReportBaseClass().hideColumn("vfree0");
                        m_bisFreeExtend = false;
                    }
                    if (voCons[i].getFieldCode().equals("isbatchext")
                            && voCons[i].getValue().equals("Y")) {
                        getReportBaseClass().showHiddenColumn("vbatchcode");
                        m_bisBatchExtend = true;
                    } else if (voCons[i].getFieldCode().equals("isbatchext")
                            && voCons[i].getValue().equals("N")) {
                        getReportBaseClass().hideColumn("vbatchcode");
                        m_bisBatchExtend = false;
                    }
                    if (voCons[i].getFieldCode().equals("iswhext")
                            && voCons[i].getValue().equals("Y")) {
                        getReportBaseClass().showHiddenColumn("storname");
                        m_bisWhExtend = true;
                    } else if (voCons[i].getFieldCode().equals("iswhext")
                            && voCons[i].getValue().equals("N")) {
                        getReportBaseClass().hideColumn("storname");
                        m_bisWhExtend = false;
                    }
                }

            }
            //清空提示信息。
            showHintMessage("");
            getReportBaseClass().setHeadDataVO(m_voHead);
            if (aryforecastvos == null || aryforecastvos.length == 0) {
                aryforecastvos = new ForecastinvVO[] { new ForecastinvVO() };
                m_alAllData = null;
                showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                        "4008report", "UPP4008report-000018")/*
                                                              * @res
                                                              * "没有查询到符合条件的结果。"
                                                              */);
                nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("Clear report result!");
                getReportBaseClass().getBillModel().clearBodyData();

            }

            //合计行
            if (m_alAllData != null && !m_alAllData.isEmpty()) {

                //String[] sReportItemKey =
                //{
                //"restnum",
                //"npraynum",
                //"npurchaseordernum",
                //"naccumchecknum",
                //"nshldtraninnum",
                //"nplannedordernum",
                //"nmanufordernum",
                //"nborrownum",
                //"nfreezenum",
                //"nwwnum",
                //"nsaleordernum",
                //"ndelivernum",
                //"nshldtranoutnum",
                //"npreparematerialnum",

                ////"nsafestocknum",
                //"nforecastnum", "navailablenum" };
                //getReportBaseClass().setAggregatedCols(sReportItemKey,
                // "invcode");
                //设置安全库存20021212
                ForecastinvVO fvo = (ForecastinvVO) m_alAllData.get(0);

            }
            //getReportBaseClass().setBodyDataVO(aryforecastvos);
            setReportData(aryforecastvos);
            if (m_alAllData != null && m_alAllData.size() > 0) {
                ForecastinvVO fvo = (ForecastinvVO) m_alAllData.get(0);

                if (fvo.getAttributeValue("nsafestocknum") != null) {
                    int columnnum = getReportBaseClass().getBodyColByKey(
                            "nsafestocknum");
                    if (getReportBaseClass().getBody_Items() != null
                            && columnnum >= 0
                            && columnnum <= getReportBaseClass()
                                    .getBody_Items().length) {

                        //getReportBaseClass().getBillModel().setValueAt(fvo.getAttributeValue("nsafestocknum"),
                        // m_alAllData.size(), columnnum);
                    }
                }
            }

            calculateTotal();

            filterNullLine();

        } catch (Exception e) {
            nc.vo.scm.pub.SCMEnv.error(e);
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "4008report", "UPP4008report-000004")/* @res "查询出错：" */
                    + e.getMessage());
        }

        return;

    }

    private void filterNullLine() {

        if (m_sReportItemKey != null && m_sReportItemKey.length > 0) {
            String sFilter = " ";

            for (int i = 0; i < m_sReportItemKey.length; i++) {
                if (i < m_sReportItemKey.length - 1) {
                    sFilter += m_sReportItemKey[i] + " != 0 || ";
                } else {
                    sFilter += m_sReportItemKey[i] + " != 0 ";
                }
            }

            getReportBaseClass().filter(sFilter);
        }

    }

    /**
     * 创建者：张欣 功能：设置新增单据的初始数据，如日期，制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
     * 修改日期，修改人，修改原因，注释标志：
     * 
     * 
     * 
     *  
     */
    protected void setBillHeadTailData() {
        try {
            nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
                    .getInstance();
            if (ce == null) {
                nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
                return;
            }

            try {
                getReportBaseClass().setTailItem("operatorcode", m_sUserCode);
                getReportBaseClass().setTailItem("operatorname", m_sUserName);

            } catch (Exception e) {

            }
            try {
                getReportBaseClass().setTailItem("unitcode", m_sCorpCode);
                getReportBaseClass().setTailItem("unitname", m_sCorpName);

            } catch (Exception e) {

            }

        } catch (Exception e) {

        }

    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-27 9:40:31)
     */
    void setFreeItemCol(boolean m_bisFreeExtend) {

        if (m_bisFreeExtend) {
            getReportBaseClass().showBodyTableCol("vfree0");
        } else {
            getReportBaseClass().hideBodyTableCol("vfree0");
        }
    }

    /**
     * 置报表表头用户选择的查询条件。 创建日期：(2001-8-23 19:44:23)
     */
    void setReportHeadItem(ConditionVO[] voCons) {
        if (voCons != null || voCons.length > 0) {
            for (int i = 0; i < voCons.length; i++) {
                if (voCons[i].getFieldCode() != null
                        && voCons[i].getFieldCode().equals("whinname")) {
                    getReportBaseClass().setHeadItem("inwhname",
                            voCons[i].getValue());
                }
                if (voCons[i].getFieldCode() != null
                        && voCons[i].getFieldCode().equals("whoutname")) {
                    getReportBaseClass().setHeadItem("outwhname",
                            voCons[i].getValue());
                }
                if (voCons[i].getFieldCode() != null
                        && voCons[i].getFieldCode().equals("invcode")) {
                    getReportBaseClass().setHeadItem("invcode",
                            voCons[i].getValue());
                }
                if (voCons[i].getFieldCode() != null
                        && voCons[i].getFieldCode().equals("vshldarrivedate")) {
                    getReportBaseClass().setHeadItem("vshldarrivedate",
                            voCons[i].getValue());
                }
                if (voCons[i].getFieldCode() != null
                        && voCons[i].getFieldCode().equals("cshlddiliverdate")) {
                    getReportBaseClass().setHeadItem("cshlddiliverdate",
                            voCons[i].getValue());
                }

                nc.vo.scm.pub.SCMEnv.out("FieldName:" + voCons[i].getFieldName());

            }

        }

    }

    /**
     * 创建者：王乃军 功能：动态加列，显示拆分字段
     * 
     * 
     * 参数： 返回： 例外： 日期：(2001-8-17 13:13:51) 修改日期，修改人，修改原因，注释标志：
     */
    public void splitField(char cGroupType, ArrayList alFieldName) {
        /*
         * try { ReportItem biReport[] = getReportBase().getBody_Items();
         * //隐藏m_ntermbeginnum等列 if (biReport == null || biReport.length == 0) {
         * nc.vo.scm.pub.SCMEnv.out("no column."); return; } // 显示的字段的数量：显示金额==3 否则==2
         * 
         * int iShowFieldNum = m_bIsMnyShow ? 3 : 2;
         * 
         * //显示的字段的数量：显示辅数量 iShowFieldNum， 否则iShowFieldNum-1 iShowFieldNum =
         * m_bIsAstNumShow ? iShowFieldNum : iShowFieldNum - 1;
         * 
         * //新增BillItem的数量名称的个数*2，如果不显示money； *3 显示money. int iFieldCount = 0;
         * 
         * if (alFieldName != null) iFieldCount = alFieldName.size();
         * //保存所有的BillItem Vector vBillItem = new Vector(); //值需要可视列 for (int
         * col = 0; col < biReport.length; col++) if (biReport[col] != null &&
         * biReport[col].isShow()) vBillItem.addElement(biReport[col]);
         * //增加收类别列,设置标题，itemkey。 ReportItem biAddItem = null; //收发类别计数器 int
         * iRdCount = 0; //分组计数器 int iGroupIdCount = 1; Vector vFieldGroup = new
         * Vector(); //分组临时变量 FldgroupVO voFg = null;
         * 
         * //分组顶层显示标题 //String sTopTitle = "入库"; //数量标题 String saColTitle[] =
         * new String[iShowFieldNum]; String saColKey[] = new
         * String[iShowFieldNum];
         * 
         * 
         * //--------------------------------------------------------
         * saColKey[0] = "nnum"; saColTitle[0]="数量";
         * 
         * //标示mny写到1或2 int iWhichWrite = 1; if (m_bIsAstNumShow) { saColKey[1] =
         * "nassitnum"; saColTitle[1]="辅数量"; iWhichWrite = 2; } if
         * (m_bIsMnyShow){ saColTitle[iWhichWrite]="金额"; saColKey[iWhichWrite] =
         * "nmny"; } //列值 int col = 0; //列名 String sColName = null;
         * 
         * for (int field = 0; field < iFieldCount; field++) { //数量 if
         * (alFieldName != null && alFieldName.size() > iRdCount &&
         * alFieldName.get(field) != null) sColName =
         * alFieldName.get(field).toString(); else sColName = " 其它 ";
         * procFieldGroupData( field, vBillItem, vFieldGroup, sColName,
         * sColName, saColKey, saColTitle, true, null); }
         * 
         * //=======================================================================
         * 
         * ReportItem riaResult[] = new ReportItem[vBillItem.size()];
         * vBillItem.copyInto(riaResult); //update billitems.
         * 
         * FldgroupVO voaFg[] = new FldgroupVO[vFieldGroup.size()];
         * vFieldGroup.copyInto(voaFg);
         * 
         * getReportBase().setFieldGroup(voaFg);
         * getReportBase().setBody_Items(riaResult); } catch (Exception e) {
         *  }
         */
    }

    /** to store the System parameters whose read from user defined parameters */

    /** BillHeaderVO */
    private String m_sHeaderVOName = "nc.vo.ic.ic602.ForecastinvHeadVO";

    /** BillItemVO */
    private String m_sItemVOName = "nc.vo.ic.ic602.ForecastinvVO";

    /** 功能节点编码 */
    private String m_sPNodeCode = "40083004";

    /** BillVO */
    private String m_sVOName = "nc.vo.ic.ic602.ForecastinvbillVO";

    /** to Store the header vo in main UI */
    private ForecastinvHeadVO m_voHead = null;

    private nc.vo.pub.AggregatedValueObject m_voReport = null;

    /**
     * ClientUI 构造子注解。
     */
    public ClientUI(nc.ui.pub.FramePanel ff) {
        super();
        setFrame(ff);
        initialize();
    }

    /**
     * 预计库存数量=现存量+预计入库数量-预计出库数量 本行的预计库存数量 = 上行的预计库存数量+ 本行的预计入-本行的预计出 本行的可用量 =
     * 上行的可用量+ 本行的预计入-本行的预计出(由于只有第一行有,现存量,所以,通用公式 变为: 本行的可用量 = 上行的可用量+ 本行现存量+
     * (本行的预计入-本行的预计出)-（冻结量（根据定义参数确定是否参与运算）＋借入数量（根据定义参数确定是否参与运算）)
     * 可用量=预计库存-（冻结量（根据定义参数确定是否参与运算）＋借入数量（根据定义参数确定是否参与运算）） 此处插入方法说明。
     * 创建日期：(2001-9-14 12:42:18)
     */
    private void calculateQty(CircularlyAccessibleValueObject cvo) {
        /*
         * UFDouble nforecastnum = null, restnum = null, navailablenum = null, //
         * 预计入 npraynum = null, npurchaseordernum = null, naccumchecknum = null,
         * nshldtransinnum = null, nplannedordernum = null, nmanufordernum =
         * null, nwwnum = null, // freezenum and borrownum nborrownum = null,
         * nfreezenum = null, // 预计出 nsaleordernum = null, ndelivernum = null,
         * nshldtransoutnum = null, npreparematerialnum = null;
         */
        UFDouble foreIn = null, foreOut = null, nforecastnum = null, restnum = null, navailablenum = null;
        UFDouble[] detailitem = null;
        /*
         * { nforecastnum, restnum, navailablenum, npraynum, npurchaseordernum,
         * naccumchecknum, nshldtransinnum, nplannedordernum, nmanufordernum,
         * nborrownum, nfreezenum, nsaleordernum, ndelivernum, nshldtransoutnum,
         * npreparematerialnum ,nwwnum };
         */
        String[] keys = { "nforecastnum", "restnum", "navailablenum",
                "npraynum", //3
                "npurchaseordernum", "naccumchecknum", "nshldtraninnum", //6
                "nplannedordernum", "nmanufordernum", "nborrownum", //9
                "nfreezenum", "nsaleordernum", //11
                "ndelivernum", "nshldtranoutnum", //13
                "npreparematerialnum", "nforecastnum", //15
                "navailablenum", "nwwnum", "nsafestocknum",//18
                "npreordernum", "ntranpraynum" };

        //////////////////
        /*
         * { "nforecastnum", "restnum", "navailablenum", "npraynum", //3
         * "npurchaseordernum", "naccumchecknum", "nshldtraninnum", //6
         * "nplannedordernum", "nmanufordernum", "nborrownum", //9 "nfreezenum",
         * "nsaleordernum", //11 "ndelivernum", "nshldtranoutnum", //13
         * "npreparematerialnum", "nwwnum", "nsafestocknum" };
         */
        //////////////////
        detailitem = new UFDouble[keys.length];
        for (int i = 0; i < keys.length; i++) {
            detailitem[i] = cvo.getAttributeValue(keys[i]) == null ? new UFDouble(
                    0)
                    : new UFDouble(cvo.getAttributeValue(keys[i]).toString());
        }
        /** to calculate the ForeInvIN */
        foreIn = detailitem[3].add(detailitem[4]).add(detailitem[5]).add(
                detailitem[6]).add(detailitem[7]).add(detailitem[8]).add(
                detailitem[17]).add(detailitem[20]);
        /** to calculate the ForeInvOUT */
        foreOut = detailitem[11].add(detailitem[12]).add(detailitem[13]).add(
                detailitem[14]).add(detailitem[19]);
        /** to gain the restnum */
        restnum = detailitem[1];
        /** to calculate the forecastnum */

        nforecastnum = detailitem[15].add(restnum.add(foreIn).sub(foreOut));

        /** to calculate the availablenum */
        navailablenum = detailitem[16].add(restnum.add(foreIn).sub(foreOut))
                .sub(detailitem[10]).sub(detailitem[9]).sub(detailitem[18]);

        /** put item qty to the VO */
        cvo.setAttributeValue("nforeIN", foreIn);
        cvo.setAttributeValue("nforeOUT", foreOut);
        cvo.setAttributeValue("nforecastnum", nforecastnum);
        cvo.setAttributeValue("navailablenum", navailablenum);

    }

    /**
     * 预计库存数量=现存量+预计入库数量-预计出库数量 本行的预计库存数量 = 上行的预计库存数量+ 本行的预计入-本行的预计出 本行的可用量 =
     * 上行的可用量+ 本行的预计入-本行的预计出(由于只有第一行有,现存量,所以,通用公式 变为: 本行的可用量 = 上行的可用量+ 本行现存量+
     * (本行的预计入-本行的预计出)-（冻结量（根据定义参数确定是否参与运算）＋借入数量（根据定义参数确定是否参与运算）)
     * 可用量=预计库存-（冻结量（根据定义参数确定是否参与运算）＋借入数量（根据定义参数确定是否参与运算）） 此处插入方法说明。
     * 创建日期：(2001-9-14 12:42:18)
     */
    private void calculateQtyNew(CircularlyAccessibleValueObject cvos[]) {
        if (cvos != null && cvos.length > 0) {
            Hashtable htKey = new Hashtable();

            UFDouble ufd0 = new UFDouble(0);
            UFDouble foreIn = null, foreOut = null, nforecastnum = null, restnum = null, navailablenum = null;
            UFDouble[] detailitem = null;
            /*
             * { nforecastnum, restnum, navailablenum, npraynum,
             * npurchaseordernum, naccumchecknum, nshldtransinnum,
             * nplannedordernum, nmanufordernum, nborrownum, nfreezenum,
             * nsaleordernum, ndelivernum, nshldtransoutnum, npreparematerialnum ,
             * nwwnum };
             */
            /*
             * String[] keys = { "nforecastnum", "restnum", "navailablenum",
             * "npraynum", //3 "npurchaseordernum", "naccumchecknum",
             * "nshldtraninnum", //6 "nplannedordernum", "nmanufordernum",
             * "nborrownum", //9 "nfreezenum", "nsaleordernum", //11
             * "ndelivernum", "nshldtranoutnum", //13 "npreparematerialnum",
             * "nwwnum", "nsafestocknum" };
             */

            //
            String[] keys = { "nforecastnum", "restnum", "navailablenum",
                    "npraynum", //3
                    "npurchaseordernum", "naccumchecknum", "nshldtraninnum", //6
                    "nplannedordernum", "nmanufordernum", "nborrownum", //9
                    "nfreezenum", "nsaleordernum", //11
                    "ndelivernum", "nshldtranoutnum", //13
                    "npreparematerialnum", "nforecastnum", //15
                    "navailablenum", "nwwnum", "nsafestocknum" };
            //
            ForecastinvVO cvo = null;
            for (int i = 0; i < cvos.length; i++) {
                cvo = (ForecastinvVO) cvos[i];

                /*
                 * UFDouble nforecastnum = null, restnum = null, navailablenum =
                 * null, // 预计入 npraynum = null, npurchaseordernum = null,
                 * naccumchecknum = null, nshldtransinnum = null,
                 * nplannedordernum = null, nmanufordernum = null, nwwnum =
                 * null, // freezenum and borrownum nborrownum = null,
                 * nfreezenum = null, // 预计出 nsaleordernum = null, ndelivernum =
                 * null, nshldtransoutnum = null, npreparematerialnum = null;
                 */

                detailitem = new UFDouble[keys.length];
                for (int j = 0; j < keys.length; j++) {
                    detailitem[j] = cvo.getAttributeValue(keys[j]) == null ? ufd0
                            : new UFDouble(cvo.getAttributeValue(keys[j])
                                    .toString());
                }
                /** to calculate the ForeInvIN */
                foreIn = detailitem[3].add(detailitem[4]).add(detailitem[5])
                        .add(detailitem[6]).add(detailitem[7]).add(
                                detailitem[8]).add(detailitem[17]);
                /** to calculate the ForeInvOUT */
                foreOut = detailitem[11].add(detailitem[12])
                        .add(detailitem[13]).add(detailitem[14]);
                /** to gain the restnum */
                restnum = detailitem[1];
                /** to calculate the forecastnum */
                nforecastnum = detailitem[15].add(restnum.add(foreIn).sub(
                        foreOut));

                /** to calculate the availablenum */
                /** 可用量中应根据用户定义的参数，减去：10 借入数量。9 冻结数量。 */
                navailablenum = detailitem[16].add(
                        restnum.add(foreIn).sub(foreOut)).sub(detailitem[10])
                        .sub(detailitem[9]);
                //由于相同的存货不能同时减安全库存, 16 安全库存数量,根据htKey判断
                if (!htKey.containsKey(cvo.getCinventoryid())) {
                    navailablenum = navailablenum.sub(detailitem[18]);
                    htKey.put(cvo.getCinventoryid(), cvo);
                }
                //debug zhx 1212
                SCMEnv.out("/** 可用量中应根据用户定义的参数，减去：10 借入数量。9 冻结数量。16 安全库存数量 */");

                /** put item qty to the VO */
                cvo.setAttributeValue("nforeIN", foreIn);
                cvo.setAttributeValue("nforeOUT", foreOut);
                cvo.setAttributeValue("nforecastnum", nforecastnum);
                cvo.setAttributeValue("navailablenum", navailablenum);
                /////////////////////////////////////////////

            }
        }

    }

    /**
     * 此处插入方法说明。 创建日期：(2001-9-19 8:54:13)
     */
    private ForecastinvVO[] construDateExtendVOs(
            CircularlyAccessibleValueObject cvo) {

        UFDouble[] accumchecknum = ((ForecastinvVO) cvo)
                .getM_aryaccumchecknum();
        UFDouble[] delivernum = ((ForecastinvVO) cvo).getM_arydelivernum();
        UFDouble[] manufordernum = ((ForecastinvVO) cvo)
                .getM_arymanufordernum();
        UFDouble[] plannedordernum = ((ForecastinvVO) cvo)
                .getM_aryplannedordernum();
        UFDouble[] praynum = ((ForecastinvVO) cvo).getM_arypraynum();
        UFDouble[] preparematerialnum = ((ForecastinvVO) cvo)
                .getM_arypreparematerialnum();
        UFDouble[] purchaseordernum = ((ForecastinvVO) cvo)
                .getM_arypurchaseordernum();
        UFDouble[] saleordernum = ((ForecastinvVO) cvo).getM_arysaleordernum();
        UFDouble[] shldtraninnum = ((ForecastinvVO) cvo)
                .getM_aryshldtraninnum();
        UFDouble[] shldtranoutnum = ((ForecastinvVO) cvo)
                .getM_aryshldtranoutnum();
        UFDouble[] wwnum = ((ForecastinvVO) cvo).getM_arywwnum();
        UFDouble[] preordernum = ((ForecastinvVO) cvo).getM_arypreordernum();
        UFDouble[] tranpraynum = ((ForecastinvVO) cvo).getM_arytranpraynum();

        int daynum = Integer.parseInt(m_sForeDays);
        ForecastinvVO[] fvos = new ForecastinvVO[daynum + 1];
        for (int i = 0; i <= daynum; i++) {
            fvos[i] = new ForecastinvVO();
            UFDate datebefore = new UFDate(m_sLogDate).getDateBefore(1);
            UFDate date = datebefore.getDateAfter(i);
            fvos[i].setAttributeValue("foreday", date.toString());
            //到货带检数量
            if (accumchecknum != null && accumchecknum[i] != null) {
                fvos[i].setAttributeValue("naccumchecknum", accumchecknum[i]);

            }
            //发货数量
            if (delivernum != null && delivernum[i] != null) {
                fvos[i].setAttributeValue("ndelivernum", delivernum[i]);

            }
            //生成订单数量
            if (manufordernum != null && manufordernum[i] != null) {
                fvos[i].setAttributeValue("nmanufordernum", manufordernum[i]);

            }
            //生成计划订单数量
            if (plannedordernum != null && plannedordernum[i] != null) {
                fvos[i].setAttributeValue("nplannedordernum",
                        plannedordernum[i]);

            }
            //请购单数量
            if (praynum != null && praynum[i] != null) {
                fvos[i].setAttributeValue("npraynum", praynum[i]);

            }
            //备料计划数量
            if (preparematerialnum != null && preparematerialnum[i] != null) {
                fvos[i].setAttributeValue("npreparematerialnum",
                        preparematerialnum[i]);

            }
            //采购订单数量
            if (purchaseordernum != null && purchaseordernum[i] != null) {
                fvos[i].setAttributeValue("npurchaseordernum",
                        purchaseordernum[i]);

            }
            //销售订单数量
            if (saleordernum != null && saleordernum[i] != null) {
                fvos[i].setAttributeValue("nsaleordernum", saleordernum[i]);

            }
            //应转入数量
            if (shldtraninnum != null && shldtraninnum[i] != null) {
                fvos[i].setAttributeValue("nshldtraninnum", shldtraninnum[i]);

            }
            //应转出数量
            if (shldtranoutnum != null && shldtranoutnum[i] != null) {
                fvos[i].setAttributeValue("nshldtranoutnum", shldtranoutnum[i]);

            }
            //委外加工订单数量
            if (wwnum != null && wwnum[i] != null) {
                fvos[i].setAttributeValue("nwwnum", wwnum[i]);

            }

            //调拨申请
            if (tranpraynum != null && tranpraynum[i] != null) {
                fvos[i].setAttributeValue("ntranpraynum", tranpraynum[i]);

            }

            //预订单
            if (preordernum != null && preordernum[i] != null) {
                fvos[i].setAttributeValue("npreordernum", preordernum[i]);

            }

            //将期初数据置入第一行：结存数量，冻结数量，借出数量，安全库存数量
            if (i == 0) {
                fvos[i]
                        .setAttributeValue("foreday", nc.ui.ml.NCLangRes
                                .getInstance().getStrByID("4008ui",
                                        "UPP4008ui-000015")/* @res " 期初余额 " */);
                fvos[i].setAttributeValue("restnum", ((ForecastinvVO) cvo)
                        .getAttributeValue("restnum"));
                fvos[i].setAttributeValue("nfreezenum", ((ForecastinvVO) cvo)
                        .getAttributeValue("nfreezenum"));
                fvos[i].setAttributeValue("nborrownum", ((ForecastinvVO) cvo)
                        .getAttributeValue("nborrownum"));
                fvos[i].setAttributeValue("nsafestocknum",
                        ((ForecastinvVO) cvo)
                                .getAttributeValue("nsafestocknum"));
            }
            //除期初余额行外的期中数据行将预计库存量和可用量置入该行VO中，以便计算。
            if (i >= 1) {

                UFDouble forenum = fvos[i - 1]
                        .getAttributeValue("nforecastnum") == null ? new UFDouble(
                        0)
                        : (UFDouble) fvos[i - 1]
                                .getAttributeValue("nforecastnum");
                UFDouble forenum2 = fvos[i - 1]
                        .getAttributeValue("navailablenum") == null ? new UFDouble(
                        0)
                        : (UFDouble) fvos[i - 1]
                                .getAttributeValue("navailablenum");
                //forenum2 = forenum2.add(forenum);
                fvos[i].setAttributeValue("nforecastnum", forenum);
                fvos[i].setAttributeValue("navailablenum", forenum2);

            }
            //计算各行期中数据的预计库存和可用量
            calculateQty(fvos[i]);

        }
        return fvos;
    }

    /**
     * 公式使用 功能： 参数： 返回： 例外： 日期：(2001-11-12 16:47:04) 修改日期，修改人，修改原因，注释标志：
     */
    private String execFormular(String formula, String value) {
        nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();

        boolean isValidity = true;

        if (formula != null && !formula.equals("")) {
            //设置表达式
            f.setExpress(formula);
            //获得变量
            nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
            //给变量付值
            Hashtable h = new Hashtable();
            for (int j = 0; j < varry.getVarry().length; j++) {
                String key = varry.getVarry()[j];

                String[] vs = new String[1];
                vs[0] = value;
                h.put(key, vs);
            }

            //设置变量值
            f.setDataS(h);
            //设置结果
            if (varry.getFormulaName() != null
                    && !varry.getFormulaName().trim().equals(""))
                return f.getValueS()[0];
            else
                return f.getValueS()[0];

        } else {
            return null;
        }
    }

    /**
     * 此处插入方法说明。 创建日期：(2003-9-24 14:20:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCorpID() {
        return m_sCorpID;
    }

    /**
     * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-5 10:38:55) 修改日期，修改人，修改原因，注释标志：
     * 
     * @return java.lang.String
     */
    public String getDefaultPNodeCode() {
        return m_sPNodeCode;
    }

    /**
     * 返回 ReportBaseClass 特性值。
     * 
     * @return nc.ui.pub.report.ReportBaseClass
     */
    /* 警告：此方法将重新生成。 */
    public ReportBaseClass getReportBaseClass() {
        if (ivjReportBase == null) {
            try {
                ivjReportBase = new nc.ui.pub.report.ReportBaseClass();
                ivjReportBase.setName("ReportBase");
                // user code begin {1}
                ivjReportBase.setRowNOShow(true);
                //ivjReportBaseClass.setParent(this);
                //ivjReportBase.setTatolRowShow(true);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                //		handleException(ivjExc);
            }
        }
        return ivjReportBase;
    }

    /**
     * 此处插入方法说明。 创建日期：(2003-9-24 14:21:06)
     * 
     * @return nc.vo.pub.AggregatedValueObject
     */
    public nc.vo.pub.AggregatedValueObject getReportVO() {
        //准备数据
        ForecastinvbillVO vo = (ForecastinvbillVO) getReportBaseClass()
                .getBillValueVO(m_sVOName, m_sHeaderVOName, m_sItemVOName);

        if (null == vo) {
            vo = new ForecastinvbillVO();
        }
        if (null == vo.getParentVO()) {
            vo.setParentVO(new ForecastinvHeadVO());
        }
        return vo;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-8-25 12:32:57)
     */
    private Hashtable getSysParams() throws Exception {
        Hashtable hticparams = null;

        try {
            /** 查询用户定义的最大展望天数 */
            m_sForeDays = SysInitBO_Client.getParaString(m_sCorpID, "IC022");
            if (m_sForeDays != null) {
                m_sForeDays = m_sForeDays.trim();
            }
        } catch (Exception e) {
            nc.vo.scm.pub.SCMEnv.error(e);
        }

        return hticparams;
    }

    /**
     * 此处插入方法说明。 创建日期：(2003-9-24 14:20:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUserID() {
        return m_sUserID;
    }

    /**
     * 此处插入方法说明。 通过加入参数，执行报表公式数组方法 作者： hanwei 2003-4-15 功能描述: 输入参数: 返回值: 异常处理:
     * 日期:
     * 
     * @param reportData
     *            nc.vo.pub.CircularlyAccessibleValueObject[]
     */
    public void setReportData(
            nc.vo.pub.CircularlyAccessibleValueObject[] reportData) {

        ArrayList arylistItemField = new ArrayList();

        //公司
        ClientCacheHelper.getColValue(reportData, new String[] { "unitcode",
                "unitname" }, "bd_corp", "pk_corp", new String[] { "unitcode",
                "unitname" }, "pk_corp");

        //库存组织
        ClientCacheHelper.getColValue(reportData, new String[] { "bodyname" },
                "bd_calbody", "pk_calbody", new String[] { "bodyname" },
                "pk_calbody");

        ClientCacheHelper
                .getColValue(reportData, new String[] { "pk_invbasdoc" },
                        "bd_invmandoc", "pk_invmandoc",
                        new String[] { "pk_invbasdoc" }, "cinventoryid");

        //基本档案
        ClientCacheHelper.getColValue(reportData, new String[] { "invname",
                "invspec", "invtype", "pk_measdoc", "invcode" },
                "bd_invbasdoc", "pk_invbasdoc", new String[] { "invname",
                        "invspec", "invtype", "pk_measdoc", "invcode" },
                "pk_invbasdoc");

        ClientCacheHelper.getColValue(reportData, new String[] { "measname" },
                "bd_measdoc", "pk_measdoc", new String[] { "measname" },
                "pk_measdoc");

      
        //自由项处理
        nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
        freeVOParse.setFreeVO(reportData, "pk_invbasdoc", null, true);

        getReportBaseClass().setBodyDataVO(reportData, true);
    }
    
    /**
	 * 排序后触发.
	 * 创建日期:(2001-10-26 14:31:14)
	 * @param key java.lang.String
	 */
	public void afterSort(String key){
		m_alAllData = (ArrayList)getRelaSortData(0);
	}
	
	/**
	 * 排序后触发.
	 * 创建日期:(2001-10-26 14:31:14)
	 * @param key java.lang.String
	 */
	public void beforeSort(String key){
		super.beforeSort(key);
		SCMEnv.out("表头排序");
		if (m_alAllData == null || m_alAllData.size() <= 0) 
			return;
		addRelaSortData(m_alAllData);
	}
}