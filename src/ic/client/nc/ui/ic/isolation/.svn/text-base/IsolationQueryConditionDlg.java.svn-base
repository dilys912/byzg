package nc.ui.ic.isolation;

import java.util.Hashtable;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic601.IC601InvOnHandHelper;
import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.mo.mo6600.mo6600;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.ic.ic601.InvOnHandHeaderVO;
import nc.vo.ic.ic601.InvOnHandItemVO;
import nc.vo.ic.ic601.InvOnHandVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;

public class IsolationQueryConditionDlg extends IcBaseReport implements ICheckCondition{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICMultiCorpQryClient ivjQueryConditionDlg = null;
	private ClientEnvironment ce = ClientEnvironment.getInstance();
	private ReportBaseClass ivjReportBase = null;
	 private AggregatedValueObject m_voReport = null;
	 boolean m_bClassflag = false;
	  boolean m_bHasAssist = false;
	  boolean m_bHasBatchcode = false;
	  boolean m_bHasCscode = false;
	  boolean m_bHasVfree0 = false;
	  private UFDate m_sLogDate = null;
	  boolean m_bHasVendor = false;
	  private Hashtable m_htShowFlag = new Hashtable();
	  boolean m_bHasHsl = false;
	  Hashtable m_htField = null;
//	private boolean m_bEverQry=false;
	public IsolationQueryConditionDlg() {
		// TODO Auto-generated constructor stub
	//	onQuery();
	}

	public String checkICCondition(ConditionVO[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	  private ICMultiCorpQryClient getConditionDlg()
	  {
	    if (this.ivjQueryConditionDlg == null) {
	      this.ivjQueryConditionDlg = new ICMultiCorpQryClient(this, this.ce.getUser().getPrimaryKey(),ce.getCorporation().getPrimaryKey(), "40083002");

	      this.ivjQueryConditionDlg.setTempletID(ce.getCorporation().getPrimaryKey(),"40083002",ce.getUser().getPrimaryKey(), null);

	      this.ivjQueryConditionDlg.setFreeItem("vfree0", "inv.invcode");

	      this.ivjQueryConditionDlg.setAutoClear("kp.pk_corp", new String[] { "kp.ccalbodyid", "wh1.storcode", "cargdoc.cscode" });

	      this.ivjQueryConditionDlg.setAutoClear("inv.invcode", new String[] { "vfree0", "vbatchcode", "meas2.pk_measdoc" });

	      this.ivjQueryConditionDlg.setAstUnit("meas2.pk_measdoc", new String[] { "kp.pk_corp", "inv.invcode" });

	      this.ivjQueryConditionDlg.setRefInitWhereClause("cargdoc.cscode", "货位档案", " bd_cargdoc.endflag='Y' and bd_cargdoc.pk_stordoc=", "wh1.storcode");

	      this.ivjQueryConditionDlg.initRefWhere("ccustomerid", " and (custflag ='0' or custflag ='2') and (bd_cumandoc.frozenflag='N' OR bd_cumandoc.frozenflag='n'  OR bd_cumandoc.frozenflag IS NULL) ");

	      this.ivjQueryConditionDlg.initRefWhere("cproviderid", " and (custflag ='1' or custflag ='3') and (bd_cumandoc.frozenflag='N' OR bd_cumandoc.frozenflag='n'  OR bd_cumandoc.frozenflag IS NULL) ");

	      this.ivjQueryConditionDlg.initRefWhere("inv.invcode", " and bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'");

	      this.ivjQueryConditionDlg.setPowerRefsOfCorp("kp.pk_corp", new String[] { "wh1.storcode", "kp.ccalbodyid", "inv.invcode", "invcl.invclasscode", "kp.cwarehouseid", "kp.cinventoryid", "cstoreadminid" }, null);

	      this.ivjQueryConditionDlg.addICheckCondition(this);
	      
	      //默认按 自由项、批号、货位展开
	      nc.vo.pub.query.QueryConditionVO[] conVOS = this.ivjQueryConditionDlg.getConditionDatas();
	      nc.vo.pub.query.QueryConditionVO convo = null;
	      for(int i=0;i<conVOS.length;i++)
	      {
	    	  convo = conVOS[i];
	    	  if(convo.getFieldCode().equalsIgnoreCase("cscodeflag"))
	    	  {
	    		  convo.setValue("Y");
	    	  }else if(convo.getFieldCode().equalsIgnoreCase("vbatchcodeflag"))
	    	  {
	    		  convo.setValue("Y");
	    	  }else if(convo.getFieldCode().equalsIgnoreCase("vfree0flag"))
	    	  {
	    		  convo.setValue("Y");
	    	  }
	      }
	    }

	    return this.ivjQueryConditionDlg;
	  }

	public InvOnHandVO onQuery()
	  {
	    if (!(this.m_bEverQry)) {
	      getConditionDlg().hideNormal();
	      getConditionDlg().showModal();
	      this.m_bEverQry = true;
	    } else {
	      getConditionDlg().onButtonConfig();
	    }

	    if (!(getConditionDlg().isCloseOK()))
	      return null;
	    String[] corps = getConditionDlg().getSelectedCorpIDs();
	    if ((corps == null) || (corps.length < 0)) {
	   //   showWarningMessage("请选择公司");
	      NCOptionPane.showConfirmDialog(this, "没有选择公司查询条件!Did not choose the company query!");
	      onQuery(true);
	      return null;
	    }

	    setDlgSubTotal(null);
	   
	      QryConditionVO voQry = new QryConditionVO();
	      voQry.setParam(0, corps);

	      ConditionVO[] voCons = getConditionDlg().getConditionVO();
	      voQry.setParam(1, adjustCondition(voCons));

	      if (!(checkConds(voCons)))
	        return null;

	      long lTime = System.currentTimeMillis();
	      long lTimes = System.currentTimeMillis();

	      try {
			InvOnHandVO vo = IC601InvOnHandHelper.queryXcl(voQry);
			 setReportData(vo.getChildrenVO(), voCons);
			 if(vo!=null ){
			//弹出输入新银行账号的窗口
			ShowResult wriui=new ShowResult(this,vo);
			 InvOnHandItemVO[] results;
				if (wriui.showModal()==1) {
					//newaccount=wriui.getUITextField1().getText();//得到新账号
					results=wriui.getResults();
				}
				else{
				return null;
				}
			if(	results!=null){
				
				InvOnHandItemVO[] result=new InvOnHandItemVO[results.length];
				InvOnHandHeaderVO head = (InvOnHandHeaderVO) vo.getParentVO();
				int a=0;
				for (int i = 0; i < results.length; i++) {  
							result[i]=(InvOnHandItemVO) vo.getChildrenVO()[results[i].getAssistnum().intValue()-1];
				}
				vo.setChildrenVO(result);
			}
				return vo;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	  }
	
	//add by zy 2019-08-15
	public InvOnHandVO onQuery2(String zdslnum, String cpid, String pgy)
	  {
	    if (!(this.m_bEverQry)) {
	      getConditionDlg().hideNormal();
	      getConditionDlg().showModal();
	      this.m_bEverQry = true;
	    } else {
	      getConditionDlg().onButtonConfig();
	    }

	    if (!(getConditionDlg().isCloseOK()))
	      return null;
	    String[] corps = getConditionDlg().getSelectedCorpIDs();
	    if ((corps == null) || (corps.length < 0)) {
	   //   showWarningMessage("请选择公司");
	      NCOptionPane.showConfirmDialog(this, "没有选择公司查询条件!Did not choose the company query!");
	      onQuery(true);
	      return null;
	    }

	    setDlgSubTotal(null);
	   
	      QryConditionVO voQry = new QryConditionVO();
	      voQry.setParam(0, corps);

	      ConditionVO[] voCons = getConditionDlg().getConditionVO();
	      voQry.setParam(1, adjustCondition(voCons));

	      if (!(checkConds(voCons)))
	        return null;

	      long lTime = System.currentTimeMillis();
	      long lTimes = System.currentTimeMillis();

	      try {
			InvOnHandVO vo = IC601InvOnHandHelper.queryXcl(voQry);
			 setReportData(vo.getChildrenVO(), voCons);
			 if(vo!=null ){
			//弹出输入新银行账号的窗口
			ShowResult wriui=new ShowResult(this,vo);
			 InvOnHandItemVO[] results;
				if (wriui.showModal()==1) {
					//newaccount=wriui.getUITextField1().getText();//得到新账号
					results=wriui.getResults();
					//把选中弹框中的数据点击确定后直接插入到库存隔离单表中 add by zy 2019.8.12
					String corp = PoPublicUIClass.getLoginPk_corp();
					if("1078".equals(corp))
					{
						IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
						UFDate date = PoPublicUIClass.getLoginDate();//获取当前时间
						final String pkCorp = results[0].m_pk_corp == null ? "" : results[0].m_pk_corp;
						final String materialNo = results[0].m_invcode == null ? "" : results[0].m_invcode;
						final String product = results[0].m_invname == null ? "" : results[0].m_invname;
						final String batchNo = results[0].m_vbatchcode == null ? "" : results[0].m_vbatchcode;
						final String cribNo = results[0].m_freevo.m_vfree1 == null ? "" : results[0].m_freevo.m_vfree1;
						UFDouble num = results[0].m_num;
						Double numm = num.toDouble();
						final int nummm = new Double(numm).intValue();
						String warehouseid = results[0].m_cwarehouseid == null ? "" : results[0].m_cwarehouseid;
						String chglid = results[0].m_cinventoryid == null ? "" : results[0].m_cinventoryid;
						final String zdnum = zdslnum;//整垛数量
//						final String sscx = scx;//生产线
//						final String djState = djzt;//单据状态
//						final String llNum = llh;//老料号
						final GlHeadVO glHeadVo = new GlHeadVO();//表头（表“mm_glzb”）
						glHeadVo.setXxrq(date);
						glHeadVo.setBillsign("isolation");//订单状态 应为isolation
						glHeadVo.setPk_corp(pkCorp);//主表pk_corp
						glHeadVo.setXxry(pgy);//品控员
						String dh = new HYPubBO().getBillNo("53", corp, null, null);//生成单号
						glHeadVo.setBillno(dh);//单号
						
						String hid = ivo.insertVO(glHeadVo);
						GlItemBVO glBodyVo = new GlItemBVO();//表体（表“mm_glzb_b”）
						glBodyVo.setLh(materialNo);//料号
						glBodyVo.setXxaglsl(nummm);//隔离数量
						glBodyVo.setCp(product);//产品名称
						glBodyVo.setIsolationcpid(chglid);//隔离产品id
						glBodyVo.setIsolationckid(warehouseid);//隔离仓库id
						glBodyVo.setPh(batchNo);//批号
						glBodyVo.setDh(cribNo);//垛号
						glBodyVo.setGlyy("并垛");//隔离原因
						glBodyVo.setPk_corp(pkCorp);//子表pk_corp
						glBodyVo.setPk_glzb(hid);
						ivo.insertVO(glBodyVo);	
						//根据cpid查询产品名称
						String sql = "select b.invname from bd_invmandoc m inner join bd_invbasdoc b " +
								"on m.pk_invbasdoc=b.pk_invbasdoc where m.pk_invmandoc='"+cpid+"'";
						IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
						ColumnListProcessor alp = new ColumnListProcessor();
						String cpo = null;
						List cplist = null;
						cplist = (List) query.executeQuery(sql, alp);
						if(cplist!=null && cplist.size()>0){
				    		cpo = cplist.get(0) == null ? "" : cplist.get(0).toString();
				    	}
						final String cp = cpo;//产品名称
						
						//打开隔离处理单节点新增状态
						SFClientUtil.openLinkedADDDialog("50081002", this, new ILinkAddData()
						{
							public String getSourceBillID() {
								return Toolkits.getString(glHeadVo.getPk_glzb());
							}
							public String getSourceBillType() {
								return "GLCL";
							}
							public String getSourcePkOrg() {
								return null;
							}
							public Object getUserObject() {
								return new Object[] {pkCorp,materialNo,product,batchNo,cribNo,zdnum,cp,nummm};
							}
						});
					}
					//end by zy
				}
				else{
				return null;
				}
			if(	results!=null){
				
				InvOnHandItemVO[] result=new InvOnHandItemVO[results.length];
				InvOnHandHeaderVO head = (InvOnHandHeaderVO) vo.getParentVO();
				int a=0;
				for (int i = 0; i < results.length; i++) {  
							result[i]=(InvOnHandItemVO) vo.getChildrenVO()[results[i].getAssistnum().intValue()-1];
				}
				vo.setChildrenVO(result);
			}
				return vo;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	  }
	//end by zy
	
	
	  private void setReportData(CircularlyAccessibleValueObject[] reportData, ConditionVO[] voCons)
	  {
	    long lTime = System.currentTimeMillis();

	    ClientCacheHelper.getColValue(reportData, new String[] { "unitcode", "unitname" }, "bd_corp", "pk_corp", new String[] { "unitcode", "unitname" }, "pk_corp");

	    ClientCacheHelper.getColValue(reportData, new String[] { "bodycode", "bodyname" }, "bd_calbody", "pk_calbody", new String[] { "bodycode", "bodyname" }, "ccalbodyid");

	    ClientCacheHelper.getColValue(reportData, new String[] { "storcode", "storname" }, "bd_stordoc", "pk_stordoc", new String[] { "storcode", "storname" }, "cwarehouseid");

	    if (this.m_bHasCscode) {
	      ClientCacheHelper.getColValue(reportData, new String[] { "cscode", "csname" }, "bd_cargdoc", "pk_cargdoc", new String[] { "cscode", "csname" }, "cspaceid");
	    }

	    ClientCacheHelper.getColValue(reportData, new String[] { "pk_invbasdoc" }, "bd_invmandoc", "pk_invmandoc", new String[] { "pk_invbasdoc" }, "cinventoryid");

	    ClientCacheHelper.getColValue(reportData, new String[] { "invname", "invcode", "invspec", "invtype", "mainmeasname" }, "bd_invbasdoc", "pk_invbasdoc", new String[] { "invname", "invcode", "invspec", "invtype", "pk_measdoc" }, "pk_invbasdoc");

	    ClientCacheHelper.getColValue(reportData, new String[] { "mainmeasname" }, "bd_measdoc", "pk_measdoc", new String[] { "measname" }, "mainmeasname");

	    if ((this.m_bHasAssist) || (this.m_bHasHsl)) {
	      ClientCacheHelper.getColValue(reportData, new String[] { "castunitname" }, "bd_measdoc", "pk_measdoc", new String[] { "measname" }, "castunitid");
	    }

	    ClientCacheHelper.getColValue(reportData, new String[] { "pk_cubasdoc" }, "bd_cumandoc", "pk_cumandoc", new String[] { "pk_cubasdoc" }, "cvendorid");

	    ClientCacheHelper.getColValue(reportData, new String[] { "custcode", "custname" }, "bd_cubasdoc", "pk_cubasdoc", new String[] { "custcode", "custname" }, "pk_cubasdoc");

	    ClientCacheHelper.getColValue(reportData, new String[] { "pk_invbasdoc" }, "bd_invmandoc", "pk_invmandoc", new String[] { "pk_invbasdoc" }, "cinventoryid");

	    BatchCodeDefSetTool.execFormulaBatchCodeForReport(reportData);

//	    SCMEnv.showTime(lTime, "普通基本档案公式解析New:");

//	    if (this.m_bHasVfree0) {
//	      lTime = System.currentTimeMillis();
//	      FreeVOParse freeVOParse = new FreeVOParse();
//	      freeVOParse.setFreeVO(reportData, "pk_invbasdoc", null, true);
//	      SCMEnv.showTime(lTime, "自由项解析");
//	    }
//	    lTime = System.currentTimeMillis();
//	    getReportBaseClass().setBodyDataVO(reportData, true);
//
//	    SCMEnv.showTime(lTime, "前台数据加载界面");
	  }

	  private void adjustColumns(Hashtable htFieldFlag)
	  {
	    if (this.m_bHasCscode) {
	      setShowFlag("cscode", true);
	      setShowFlag("csname", true);
	    } else {
	      setShowFlag("cscode", false);
	      setShowFlag("csname", false);
	    }

	    if (this.m_bClassflag) {
	      setShowFlag("invclasscode", true);
	      setShowFlag("invclassname", true);
	      setShowFlag("invcode", false);
	      setShowFlag("invname", false);
	      setShowFlag("invspec", false);
	      setShowFlag("invtype", false);
	      setShowFlag("mainmeasname", false);
	      setShowFlag("castunitname", false);
	      setShowFlag("hsl", false);
	      setShowFlag("vbatchcode", false);
	      setShowFlag("dvalidate", false);
	      setShowFlag("vfree0", false);
	      setShowFlag("assistnum", false);
	    }
	    else {
	      setShowFlag("invclasscode", false);
	      setShowFlag("invclassname", false);
	      setShowFlag("invcode", true);
	      setShowFlag("invname", true);
	      setShowFlag("invspec", true);
	      setShowFlag("invtype", true);
	      setShowFlag("mainmeasname", true);

	      if (this.m_bHasAssist) {
	        setShowFlag("castunitname", true);
	        setShowFlag("hsl", true);
	        setShowFlag("assistnum", true);
	      } else {
	        setShowFlag("castunitname", true);
	        setShowFlag("hsl", false);
	        setShowFlag("assistnum", false);
	      }

	      if (this.m_bHasBatchcode) {
	        setShowFlag("vbatchcode", true);
	        setShowFlag("dvalidate", true);
	      } else {
	        setShowFlag("vbatchcode", false);
	        setShowFlag("dvalidate", false);
	      }

	      if (this.m_bHasVfree0) {
	        setShowFlag("vfree0", true);
	      }
	      else {
	        setShowFlag("vfree0", false);
	      }

	      setShowFlag("custcode", this.m_bHasVendor);
	      setShowFlag("custname", this.m_bHasVendor);
	      setShowFlag("hsl", this.m_bHasHsl);
	    }
	  }
	  public void setShowFlag(String strKey, boolean flag)
	  {
	    if (flag) {
	      if ((this.m_htShowFlag.containsKey(strKey)) && 
	        (!(((Boolean)this.m_htShowFlag.get(strKey)).booleanValue())))
	      {
	        getReportBaseClass().showBodyTableCol(strKey);

	        this.m_htShowFlag.put(strKey, new Boolean(true));
	      }

	    }
	    else if ((this.m_htShowFlag.containsKey(strKey)) && 
	      (((Boolean)this.m_htShowFlag.get(strKey)).booleanValue()))
	    {
	      getReportBaseClass().hideBodyTableCol(strKey);

	      this.m_htShowFlag.put(strKey, new Boolean(false));
	    }
	  }
	  private ConditionVO[] adjustCondition(ConditionVO[] voCons)
	  {
	    return getConditionDlg().getExpandVOs(voCons);
	  }
	  private boolean checkConds(ConditionVO[] cons)
	  {
	    String sField = null;
	    boolean bcstoreadminid = false;
	    boolean binvclass = false;
	    for (int i = 0; i < cons.length; ++i) {
	      sField = cons[i].getFieldCode();
	      if (sField.equals("cstoreadminid"))
	        bcstoreadminid = true;
	      else if ((sField.equals("classflag")) && (cons[i].getValue().equalsIgnoreCase("Y")))
	        binvclass = true;
	    }

	    if ((bcstoreadminid) && (binvclass)) {
	      showErrorMessage(ResBase.get601AdminAndClass());
	      return false;
	    }
	    return true;
	  }
	  protected void showTime(long lStartTime, String sTaskHint)
	  {
	    long lTime = System.currentTimeMillis() - lStartTime;
	    SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为：" + (lTime / 60000L) + "分" + (lTime / 1000L % 60L) + "秒" + (lTime % 1000L) + "毫秒");
	  }
	  public InvOnHandHeaderVO getHeader(ConditionVO[] voCons)
	  {
	    InvOnHandHeaderVO voHead = new InvOnHandHeaderVO();

	    voHead.setQuerydate(ce.getDate());
	    for (int i = 0; i < voCons.length; ++i) {
	      RefResultVO ref;
	      if (voCons[i].getFieldCode().equals("kp.ccalbodyid")) {
	        ref = voCons[i].getRefResult();
	        if (ref != null)
	          voHead.setCcalbodyname(ref.getRefName());
	      }

	      if (voCons[i].getFieldCode().equals("kp.pk_corp")) {
	        ref = voCons[i].getRefResult();
	        if (ref != null)
	          voHead.setUnitname(ref.getRefName());
	      }
	    }

	    voHead.setQuerycondition(getConditionDlg().getChText());
	    return voHead;
	  }
	@Override
	public String getCorpID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultPNodeCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportBaseClass getReportBaseClass() {
		// TODO Auto-generated method stub
		 if (this.ivjReportBase == null) {
		      try {
		        this.ivjReportBase = new ReportBaseClass();
		        this.ivjReportBase.setName("ReportBase");
		        super.addSortListener();
		      }
		      catch (Throwable ivjExc)
		      {
		      }

		    }

		    return this.ivjReportBase;
	}

	@Override
	public AggregatedValueObject getReportVO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onQuery(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	public Hashtable getFieldFlag(ConditionVO[] voCons)
	  {
	    Hashtable htRes = new Hashtable();

	    if ((voCons == null) || (voCons.length < 1))
	      return htRes;

	    for (int i = 0; i < voCons.length; ++i) {
	      String sItemkey = voCons[i].getFieldCode();

	      if (!(htRes.containsKey(sItemkey)))
	        htRes.put(sItemkey, voCons[i].getValue());
	    }

	    return htRes;
	  }
	  private Hashtable getField(ConditionVO[] voCons)
	  {
	    int i;
	    this.m_htField = new Hashtable();
	    if ((voCons != null) && (voCons.length > 0))
	    {
	      for (i = 0; i < voCons.length; ++i) {
	        String sItemkey = voCons[i].getFieldCode();

	        if (!(this.m_htField.containsKey(sItemkey)))
	          this.m_htField.put(sItemkey, voCons[i].clone());
	      }
	    }
	    if ((this.m_htField.containsKey("classflag")) && (((ConditionVO)this.m_htField.get("classflag")).getValue().equalsIgnoreCase("Y")))
	    {
	      this.m_bClassflag = true;
	    }
	    else this.m_bClassflag = false;

	    if ((this.m_htField.containsKey("meas2.assistnum")) || (this.m_htField.containsKey("bd_measdoc2.measname")) || ((this.m_htField.containsKey("measnameflag")) && (((ConditionVO)this.m_htField.get("measnameflag")).getValue().equalsIgnoreCase("Y"))))
	    {
	      this.m_bHasAssist = true;
	    }
	    else this.m_bHasAssist = false;

	    if ((this.m_htField.containsKey("cargdoc.cscode")) || ((this.m_htField.containsKey("cscodeflag")) && (((ConditionVO)this.m_htField.get("cscodeflag")).getValue().equalsIgnoreCase("Y"))))
	    {
	      SCMEnv.out("m_bHasCscode = true;");

	      this.m_bHasCscode = true;
	    } else {
	      this.m_bHasCscode = false;
	      SCMEnv.out("m_bHasCscode = false;");
	    }

	    if ((this.m_htField.containsKey("vbatchcode")) || (this.m_htField.containsKey("dvalidate")) || ((this.m_htField.containsKey("vbatchcodeflag")) && (((ConditionVO)this.m_htField.get("vbatchcodeflag")).getValue().equalsIgnoreCase("Y"))))
	    {
	      this.m_bHasBatchcode = true;
	    }
	    else this.m_bHasBatchcode = false;

	    if ((this.m_htField.containsKey("vfree1")) || (this.m_htField.containsKey("vfree2")) || (this.m_htField.containsKey("vfree3")) || (this.m_htField.containsKey("vfree4")) || (this.m_htField.containsKey("vfree5")) || (this.m_htField.containsKey("vfree6")) || (this.m_htField.containsKey("vfree7")) || (this.m_htField.containsKey("vfree8")) || (this.m_htField.containsKey("vfree9")) || (this.m_htField.containsKey("vfree10")) || ((this.m_htField.containsKey("vfree0flag")) && (((ConditionVO)this.m_htField.get("vfree0flag")).getValue().equalsIgnoreCase("Y"))))
	    {
	      this.m_bHasVfree0 = true;
	    }
	    else this.m_bHasVfree0 = false;

	    if ((this.m_htField.containsKey("vendor.custcode")) || ((this.m_htField.containsKey("cvendorflag")) && (((ConditionVO)this.m_htField.get("cvendorflag")).getValue().equalsIgnoreCase("Y"))))
	    {
	      this.m_bHasVendor = true;
	    }
	    else this.m_bHasVendor = false;

	    if ((this.m_htField.containsKey("hslflag")) && (((ConditionVO)this.m_htField.get("hslflag")).getValue().equalsIgnoreCase("Y")))
	      this.m_bHasHsl = true;
	    else
	      this.m_bHasHsl = false;

	    return this.m_htField;
	  }
	  
	  public InvOnHandVO doQuery()
	  {
	    if (!(this.m_bEverQry)) {
	      getConditionDlg().hideNormal();
	      getConditionDlg().showModal();
	      this.m_bEverQry = true;
	    } else {
	      getConditionDlg().onButtonConfig();
	    }

	    if (!(getConditionDlg().isCloseOK()))
	      return null;
	    String[] corps = getConditionDlg().getSelectedCorpIDs();
	    if ((corps == null) || (corps.length < 0)) {
	   //   showWarningMessage("请选择公司");
	      NCOptionPane.showConfirmDialog(this, "没有选择公司查询条件!Did not choose the company query!");
	      onQuery(true);
	      return null;
	    }

	    setDlgSubTotal(null);
	   
	      QryConditionVO voQry = new QryConditionVO();
	      voQry.setParam(0, corps);

	      ConditionVO[] voCons = getConditionDlg().getConditionVO();
	      voQry.setParam(1, adjustCondition(voCons));

	      if (!(checkConds(voCons)))
	        return null;

	      long lTime = System.currentTimeMillis();
	      long lTimes = System.currentTimeMillis();

	      try {
	    	
			InvOnHandVO vo = IC601InvOnHandHelper.queryXcl(voQry);
			
			 setReportData(vo.getChildrenVO(), voCons);
			 if(vo!=null ){
			//弹出输入新银行账号的窗口
			ShowResult wriui=new ShowResult(this,vo);
			 InvOnHandItemVO[] results;
				if (wriui.showModal()==1) {
					//newaccount=wriui.getUITextField1().getText();//得到新账号
					results=wriui.getResults();
				}
				else{
				return null;
				}
			if(	results!=null){
				
				InvOnHandItemVO[] result=new InvOnHandItemVO[results.length];
				InvOnHandHeaderVO head = (InvOnHandHeaderVO) vo.getParentVO();
				int a=0;
				for (int i = 0; i < results.length; i++) {  
							result[i]=(InvOnHandItemVO) vo.getChildrenVO()[results[i].getAssistnum().intValue()-1];
				}
				vo.setChildrenVO(result);
			}
				return vo;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	  }
}
