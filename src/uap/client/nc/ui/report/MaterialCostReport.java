package nc.ui.report;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import nc.bs.framework.common.NCLocator;
import nc.bs.ia.reportalgo.SQLStringUtil;
import nc.impl.ia.pub.CommonDataImpl;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ia.analyze.IaAnalyseBO_Client;
import nc.ui.ia.ia604.InOutSumQueryUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.report.base.ReportUIBase;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.mm.pub.pub1020.BomItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.report.MaterialCostBVO;

public class MaterialCostReport extends ReportUIBase  {


			/**
			 * 功能：
			 * @author 胡恺
			 * @Time 
			 */
			private static final long serialVersionUID = 1L;
			public static final String ModuleCode = "201490A1";
			private ButtonObject EXCEL_btn  = new ButtonObject("输出EXCEL","输出EXCEL");
			static ClientEnvironment ce =ClientEnvironment.getInstance();
			
			public MaterialCostReport()
			{
				super();
				
				this.getConditionPanel().setVisible( false );	// 将查询结果面板设为不显示
				this.getReportBase().setShowThMark( true );		// 显示会计分位
				this.getReportBase().setBodyMenuShow( false );	// 屏蔽表体的右键菜单
				
				// 设置所显示的按钮
				// 0查询 1刷新 2交叉设置 3栏目设置 4小计合计 5过滤 6分组 7排序 8打印
				this.setButtonsz();
				this.getReportBase().getBillTable().addMouseListener(new MouseAdapter(){

					public void mouseClicked(MouseEvent e) {
						
					}

					});
				
			}
			/**
			 * 如需自定义按钮,重载此方法，此方法在初始化时调用。
			 */
			 protected void updateAllButtons() {
				  this.setButtonsz();
				  updateButtons();
			 }
			 
			public void setButtonsz() {
				ButtonObject[] btns = this.getButtons();
				this.setButtons( new ButtonObject[]{
						btns[0],
						btns[1],
						btns[2],
						btns[3],
						btns[4],
						btns[5],
						btns[6],
						btns[7],
						btns[8],
						EXCEL_btn
						}
				);
			}
			
			public void onButtonClicked(ButtonObject btn) {
				super.onButtonClicked(btn);
				
				if( EXCEL_btn==btn )	// 按网点输出Excel
				{
					
					  /************ 此处三个final变量因为在线程内部类中调用，所以声明为final类型 **************/
		            // 定义文件选择对话框类
		            UIFileChooser chooser = getFileChooser();

		    		// 调用显示界面方法
		    		int ret = chooser.showDialog(MaterialCostReport.this, "打开文件");
		    		// 如果取消，则返回
		    		if (ret == 1)
		    			return;
		            // 定义File对象，
		            final File file = chooser.getSelectedFile();
		            // 将导出的整个界面
		            final UITable dbtable = this.getReportBase().getBillTable();;
		            if (file == null) {
		                    return;
		            }
		            /******* 由于导入导出查询等操作都较耗时，所以用如下线程弹出进度对话框，避免出现“假死”现象 **********/
		            // 线程类
		            Runnable checkRun = new Runnable() {
		                    public void run() {
		                            // 线程对话框：系统运行提示框
		                            BannerDialog dialog = new BannerDialog(getParent());
		                            dialog.start();
		                            try {
		                                    /**
		                                     * 将导入导出查询等方法写在其中
		                                     */
		                            	ExportExcel.writeJxlByTableModel(file.getAbsolutePath(),dbtable);
		                            } catch (Exception e) {
		                                    e.printStackTrace();
		                                    showErrorMessage(e.getMessage());
		                            } finally {
		                                    // 销毁系统运行提示框
		                                    dialog.end();
		                            }
		                    }
		            };
		            // 启用线程
		            new Thread(checkRun).start();
				}
			}

			// 选择文件对话框
		    @SuppressWarnings("static-access")
			private static UIFileChooser getFileChooser() {
		            UIFileChooser bomFileChooser_imp = null;
		            if (bomFileChooser_imp == null) {
		                    bomFileChooser_imp = new UIFileChooser();
		                    // 表示可以选取目录
		                    bomFileChooser_imp.setFileSelectionMode(UIFileChooser.FILES_AND_DIRECTORIES);
		                    // 设置默认文件名
		                    String ts = getString(ce.getDate())+getString(ce.getServerTime()).replaceAll(":", "");
		                    bomFileChooser_imp.setSelectedFile(new File("报表数据--"+ts+".xls"));
		                    // 设置对话框标题
		                    bomFileChooser_imp.setDialogTitle("选择文件对话框");
		                    bomFileChooser_imp.setBounds(325, 500, 25, 25);
		                    bomFileChooser_imp.setAcceptAllFileFilterUsed(false);
		                    bomFileChooser_imp.setFileFilter(new FileFilter() {
		                            public boolean accept(File f) { // 设定可用的文件的后缀名
		                                    if (f.getName().toLowerCase().endsWith(".xls")
		                                                    || f.isDirectory()) {
		                                            return true;
		                                    }
		                                    return false;
		                            }

		                            public String getDescription() {
		                                    return "EXCEL文档(*.xls)";
		                            }
		                    });
		            }
		            return bomFileChooser_imp;
		    }
			
			@Override
			public String getModuleCode() {
				return ModuleCode;
			}
			
			ReportQDLG queryDLG = null;
			@Override
			public ReportQDLG getQryDlg() {
				if( queryDLG==null )
				{
					queryDLG = new ReportQDLG( this );
					queryDLG.setTempletID( 
							this.getCorpPrimaryKey(), 
							ModuleCode,
							this.getUIControl()._getOperator(),
							null
					);
				}
				return queryDLG;
			}
			
			/**
			 * 按照模板打印
			 */
			protected void onPrintTemplet() {
				nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(ModuleCode, getReportBase());
				nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
						dataSource);
				print.setTemplateID(ce.getCorporation().getPrimaryKey(), ModuleCode, ce.getUser().getPrimaryKey(), null, null);
				if (print.selectTemplate() == 1)
					print.preview();
			}
			
			/**
			 * 功能：依据查询条件查出账龄数据并在窗口显示
			 * @author 
			 * @time 2012-7-20
			 * */
			public void onQuery(){

			    if (this.m_dlgQuery == null) {
			      this.m_dlgQuery = new InOutSumQueryUI(this, "", this.ce.getUser().getPrimaryKey(), this.ce.getCorporation().pk_corp);

			      this.m_dlgQuery.setRefWherePart(this.m_sCorpID);
			    }
			    this.m_dlgQuery.showModal();
			    if (this.m_dlgQuery.isCloseOK()) {
			      try {
			        this.m_voQuery = this.m_dlgQuery.getQueryVO();
			        this.m_voQuery.setConditionVO(this.m_dlgQuery.getConditionVO());
			        String[] sCorps = this.m_voQuery.getPk_Corps();
			        StatisticsVO svo = new StatisticsVO();
			        svo.setFieldCode("cinventoryid");
			        svo.setFieldName("存货");
			        StatisticsVO[] svoz = new StatisticsVO[1];
			        svoz[0] = svo;
			        this.m_voQuery.setStatistics(svoz);
			        this.m_voQuery.SourceTable = QueryVO.INOUTSUM;
			        setReportData(getQryDlg().getConditionVO());
			      }
			      catch (Exception e) {
			        e.printStackTrace();
			      }
			    }
			  
			}
			String m_sCorpID = "1078";
			private QueryVO m_voQuery = null;
			protected InOutSumQueryUI m_dlgQuery = null;
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(
			  		  IUAPQueryBS.class.getName());
			
			/**
			 * 功能：依据查询条件查出数据并在窗口显示
			 * @author 胡恺
			 * @time 2012-12-09
			 * */
			@SuppressWarnings({ "unchecked" })
			public void setReportData(ConditionVO[] con) throws BusinessException, ClassNotFoundException {
				String sCorpAndPeriod = parseCorpAndPeriod(m_voQuery);
				String whereSql = sCorpAndPeriod==null?"1=1":sCorpAndPeriod;
				
				StringBuffer clcksql = new StringBuffer();
				
				clcksql.append("  select chsl.cinventoryid chzj,chbas.invcode chbm,chbas.invname chmc,mea.measname chdw,chsl.sl sjyl ,dh.sl/1000 bzdh ") 
				.append("         from (select b.cinventoryid,sum(b.noutnum) sl  ") 
				.append("         from ic_general_b b left join ic_general_h h on h.cgeneralhid = b.cgeneralhid and nvl(b.dr,0)=0   ") 
				.append("         where h.pk_corp = '1078'and h.cbilltypecode = '4D' and nvl(h.dr,0)=0 and h.cdispatcherid = '1078A210000000013VY7'   and "+whereSql) 
				.append("         group by b.cinventoryid having sum(b.noutnum)<>0 ) chsl  ") 
				.append("         left join bd_invmandoc ch on chsl.cinventoryid = ch.pk_invmandoc  ") 
				.append("         left join bd_invbasdoc chbas on ch.pk_invbasdoc = chbas.pk_invbasdoc  ") 
				.append("         left join bd_measdoc mea on chbas.pk_measdoc = mea.pk_measdoc  ") 
				.append("         left join (select distinct invb.invcode,bom.sl from bd_bom_b bom left join bd_invbasdoc invb on bom.zxbmid = invb.pk_invbasdoc ") 
				.append("         left join bd_bom zb on zb.pk_bomid = bom.pk_bomid ") 
				.append("         where bom.pk_corp = '1078' and nvl(bom.dr,0)=0 and nvl(bom.dr,0)=0 and zb.version = '1.0' ) dh on chbas.invcode = dh.invcode ") 
				.append("         order by chbas.invcode  ") ;
				
				List clcklist = (List) sessionManager.executeQuery(clcksql.toString(), new BeanListProcessor(MaterialCostBVO.class));
				
				ArrayList alResult = null;
				try {
					alResult = IaAnalyseBO_Client.getInOutSum(m_voQuery);
				} catch (Exception e) {
					e.printStackTrace();
				}
				InvInOutSumVO[] m_voaInOutSum = ((InvInOutSumVO[])(InvInOutSumVO[])alResult.get(0));
			      Hashtable htRdInName = (Hashtable)alResult.get(1);
			      Hashtable htRdOutName = (Hashtable)alResult.get(2);
			     List list = new ArrayList();
			    for (int i = 0; i < clcklist.size(); i++) {
			    	MaterialCostBVO mvo = (MaterialCostBVO) clcklist.get(i);
			    	String pk_invbas = mvo.getChzj();
			    	for (int j = 0; j < m_voaInOutSum.length; j++) {
			    		InvInOutSumVO inoutvo = m_voaInOutSum[j];
			    		if(pk_invbas.equals(inoutvo.getCinventoryid())){
			    			UFDouble mny = inoutvo.getNbeginmny().add(inoutvo.getNinmny()).sub(inoutvo.getNoutmny());
			    			UFDouble num = inoutvo.getNbeginnum().add(inoutvo.getNinnum()).sub(inoutvo.getNoutnum());
			    			mvo.setDj(mny.div(num));
			    			mvo.setJe(mvo.getDj().multiply(mvo.getSjyl()));
			    		}
					}
			    	mvo.setXh(i+1);
			    	list.add(mvo);
				}
			      
				this.getReportBase().setBodyDataVO((CircularlyAccessibleValueObject[]) list.toArray(new MaterialCostBVO[0]));
				
				
//				  //显示合计项
//	            String[] strsubValKeys =new  String[]
//	            {"htczj"};
//	            String[] strgrpValKeys = {"xq"};                        
//	            SubtotalContext stctx = new SubtotalContext();
//	            stctx.setGrpKeys(strgrpValKeys);
//	            stctx.setSubtotalCols(strsubValKeys);        //配置要进行合计的字段
//	            stctx.setIsSubtotal(false);                  //需要小计
//	            stctx.setLevelCompute(true);
//	            stctx.setSubtotalName("小计");
//	            stctx.setTotalNameColKeys("xq");        //设置合计项显示列位置
//	            stctx.setSumtotalName("合计");                     //设置合计项显示名称
//	            this.getReportBase().setSubtotalContext(stctx);
//
//	            this.getReportBase().subtotal();
				

		    }
			
			 
			private UFDouble getmonths(UFDate bstartdate, UFDate benddate) {
		 		UFDouble btotlerent1 = new UFDouble(0,2);
				UFDouble btotlerent2 = new UFDouble(0,2);
				UFDouble btotlerent3 = new UFDouble(0,2);
				UFDouble btotlerent4 = new UFDouble(0,2);
				UFDouble btotlarent = new UFDouble(0,2);
				btotlerent1 = (getUFDouble(bstartdate.getDaysMonth() - bstartdate.getDay() + 1).div(getUFDouble(bstartdate.getDaysMonth())));
				btotlerent2 = (getUFDouble(benddate.getDay()).div(getUFDouble(benddate.getDaysMonth())));
				btotlerent3 = (getUFDouble(benddate.getMonth() - bstartdate.getMonth()-1));
				btotlerent4 = (getUFDouble((benddate.getYear() - bstartdate.getYear()) * 12));
				btotlarent = btotlarent.add(btotlerent1.add(btotlerent2.add(btotlerent3.add(btotlerent4))));
				return btotlarent;
			}
			public String cutZero(String v){
				if(v.indexOf(".") > -1){
				   while(true){
				      if(v.lastIndexOf("0") == (v.length() - 1)){
				         v = v.substring(0,v.lastIndexOf("0"));
				      }else{
				         break;
				      }
				   }
				   if(v.lastIndexOf(".") == (v.length() - 1)){
				      v = v.substring(0, v.lastIndexOf("."));
				   }
				}
				   return v;
		      }
			  public static String getString(Object obj){
				  if(!isEmpty(obj)){
					  try{
						  return obj==null?"":obj.toString().trim();
					  }catch(Exception e){
						  return "";
					  }
				  }else{
					  return "";
				  }
			  }
		    
			  public static UFDate getUFDate(Object obj){
				  if(isEmpty(obj)){
					  try{
						  return new UFDate(obj.toString().trim());
					  }catch(Exception e){
						  return null;
					  }
				  }else{
					  return null;
				  }
			  }
			  public static UFDouble getUFDouble(Object obj){
				  if(isEmpty(obj)){
					  try{
						  return new UFDouble(obj.toString().trim());
					  }catch(Exception e){
						  return new UFDouble(0);
					  }
				  }else{
					  return new UFDouble(0);
				  }
			  }
			  public  UFDouble getUFDouble(String obj){
				  if(!isEmpty(obj)){
					  try{
						  return new UFDouble(obj.trim());
					  }catch(Exception e){
						  return new UFDouble(0);
					  }
				  }else{
					  return new UFDouble(0);
				  }
			  }
			    @SuppressWarnings("unchecked")
				public static boolean isEmpty(Object value) {
			        if (value == null){
			            return true;
			        }
			        if ((value instanceof String) && (((String) value).trim().length() <= 0)){
			            return true;
			        }
			        if ((value instanceof Object[]) && (((Object[]) value).length <= 0)) {
			            return true;
			        }
			        //判断数组中的值是否全部为空null.
			        if (value instanceof Object[]) {
			            Object[] t = (Object[]) value;
			            for (int i = 0; i < t.length; i++) {
			                if (t[i] != null) {
			                    return false;
			                }
			            }
			            return true;
			        }
			        if ((value instanceof Collection) && ((Collection) value).size() <= 0){
			            return true;
			        }
			        if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0){
			            return true;
			        }
			        if ((value instanceof Map) && ((Map) value).size() <= 0){
			            return true;
			        }
			        return false;
			    }

			    private String parseCorpAndPeriod(QueryVO query)
			      throws BusinessException
			    {
			      String[] corps = query.getPk_Corps();
			      String beginDate = query.getDate()[0];
			      String endDate = query.getDate()[1];

			      String result = "";
			      String corp = "";
			        if (corps.length == 1) {
			          corp = corp + " h.pk_corp='" + corps[0] + "' ";
			        }
			        else {
			          corp = corp + " h.pk_corp in (";
			          for (int i = 0; i < corps.length; i++) {
			            if (i != 0) {
			              corp = corp + ",";
			            }
			            corp = corp + "'" + corps[i] + "'";
			          }
			          corp = corp + ")";
			        }
			        result = (corp + " and h.dbilldate>='" + beginDate + "' and h.dbilldate<='" + endDate + "' ");
			      return result;
			    }
		

	}


