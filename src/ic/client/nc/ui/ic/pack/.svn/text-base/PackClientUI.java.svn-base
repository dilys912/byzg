package nc.ui.ic.pack;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.report.base.ReportUIBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

@SuppressWarnings("deprecation")
public class PackClientUI extends ReportUIBase {

	/**
	 * 功能：包装物回收比例明细表报表
	 * @author 王凯飞
	 * @Time 2013-01-15
	 */
	private static final long serialVersionUID = 1L;
	String vdate = "";
	String pk_corp = "";
	String mindate = "";
	public static final String ModuleCode = "40083430";
	private ButtonObject EXCEL_btn  = new ButtonObject("输出EXCEL","输出EXCEL");
	static ClientEnvironment ce =ClientEnvironment.getInstance();
	
	public PackClientUI()
	{
		super();
		
		this.getConditionPanel().setVisible( false );	// 将查询结果面板设为不显示
		this.getReportBase().setShowThMark( true );		// 显示会计分位
		this.getReportBase().setBodyMenuShow( false );	// 屏蔽表体的右键菜单
		
		// 设置所显示的按钮
		// 0查询 1刷新 2交叉设置 3栏目设置 4小计合计 5过滤 6分组 7排序 8打印
		this.setButtonsz();
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
    		int ret = chooser.showDialog(PackClientUI.this, "打开文件");
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
            /***************** 由于导入导出查询等操作都较耗时，所以用如下线程弹出进度对话框，避免出现“假死”现象 ************************/
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
                    bomFileChooser_imp.setSelectedFile(new File("包装物回收比例明细表--"+ts+".xls"));
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
	 * 功能：查询
	 * @author wkf
	 * @time 2014-4-10
	 * */
	public void onQuery(){
		getQryDlg().hideNormal();
		if (getQryDlg().showModal() == QueryConditionClient.ID_CANCEL) {
			return;
		}
		//删除上次查询的显示条件
		getConditionPanel().removeAll();
		//清楚表体显示数据
		this.getReportBase().setBodyDataVO(null);		
		//加载数据
		Runnable checkRun = new Runnable() {
			public void run() {
				BannerDialog dialog = new BannerDialog(	getParent());
				dialog.start();
				try {
					setReportData(getQryDlg().getConditionVO());
				} catch (Exception e) {
					e.printStackTrace();
					showErrorMessage(e.getMessage());
				} finally {
					dialog.end();
				}
			}

		};
		new Thread(checkRun).start();
	}
	public List<String> getCust(String whereSql){
		return null;
	}
	private HashMap<String, String> zuzhuang(String wheresql) {
		HashMap<String, String> wheres = new HashMap<String, String>();
		String[] whs = wheresql.split("and");
		for (int i = 0; i < whs.length; i++) {
			String sql = whs[i].toString();
			if(sql.contains("dbilldatestart")){
				int dengh = sql.indexOf("=");
				String sql1 = sql.substring(dengh+3, dengh+13);
				wheres.put("dbilldatestart", sql1);
			}
			if(sql.contains("dbilldateend")){
				int dengh = sql.indexOf("=");
				String sql1 = sql.substring(dengh+3, dengh+13);
				wheres.put("dbilldateend", sql1);
			}
			if(sql.contains("pk_cubasdoc")){
				int dengh = sql.indexOf("=");
				String sql1 = sql.substring(dengh+3, dengh+23);
				wheres.put("pk_cubasdoc", sql1);
			}
		}
		
		return wheres;
	}
	@SuppressWarnings({ "unchecked" })
	public void setReportData(ConditionVO[] con) throws BusinessException, ClassNotFoundException {
		String whereSql = getQryDlg().getWhereSQL()==null?"1=1":getQryDlg().getWhereSQL();
		HashMap<String, String> wheres  = zuzhuang(whereSql);
		String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();
		StringBuffer sql = new StringBuffer();
		sql.append(" select custname,pk_cubasdoc,nvl(sum(numtao),0) vdef0, nvl(sum(tuopan),0) vdef1, nvl(sum(dinggai),0) vdef2, nvl(sum(dianguangzhi),0) vdef3 ") 
		.append(" from ((select custname,pk_cubasdoc,0 numtao,sum(tuopan) tuopan,sum(dinggai) dinggai,sum(dianguangzhi) dianguangzhi  ") 
		.append("      from(select cub.custname,cub.pk_cubasdoc pk_cubasdoc, ") 
		.append("                CASE ") 
		.append("                  when substr(inv.invcode,0,8) = '25020101' then sum(b.ninnum) ") 
		.append("                end tuopan, ") 
		.append("                CASE ") 
		.append("                  when substr(inv.invcode,0,8) = '25020102' then sum(b.ninnum) ") 
		.append("                end dinggai, ") 
		.append("                CASE ") 
		.append("                  when substr(inv.invcode,0,8) = '25020103' then sum(b.ninnum) ") 
		.append("                end dianguangzhi ") 
		.append("           from ic_general_h h, ic_general_b b, bd_rdcl rd, bd_cubasdoc cub, bd_cumandoc cum,bd_invbasdoc inv ") 
		.append("          where h.cgeneralhid = b.cgeneralhid ") 
		.append("            and h.cdispatcherid = rd.pk_rdcl ") 
		.append("            and h.cproviderid = cum.pk_cumandoc ") 
		.append("            and cub.pk_cubasdoc = cum.pk_cubasdoc ") 
		.append("            and b.cinvbasid = inv.pk_invbasdoc ") 
		.append("            and rd.rdcode in ('0102') ") 
		.append("            and rd.pk_corp = '"+pk_corp+"' ") ;
		if(wheres.get("dbilldatestart")==null || wheres.get("dbilldatestart").equals(null)){
			sql.append("            and h.dbilldate >= '' or ''  is null ") ; 
		}else{
			sql.append("            and h.dbilldate >= '"+wheres.get("dbilldatestart")+"' or '"+wheres.get("dbilldatestart")+"'  is null ") ;
		}
		if(wheres.get("dbilldateend")==null || wheres.get("dbilldateend").equals(null)){
			sql.append("            and h.dbilldate <= '' or ''  is null ") ; 
		}else{
			sql.append("            and h.dbilldate <= '"+wheres.get("dbilldateend")+"' or '"+wheres.get("dbilldateend")+"'  is null ") ;
		}
		sql.append("            and h.pk_corp = '"+pk_corp+"' ") 
		.append("            and nvl(b.dr,0) = 0 ") 
		.append("            and nvl(h.dr,0) = 0 ") 
		.append("            group by cub.custname,cub.pk_cubasdoc,inv.invcode) ") 
		.append("            group by custname,pk_cubasdoc) ") 
		.append(" union all(select cub.custname,cub.pk_cubasdoc,count(b.vfree1) numtao,0 tuopan,0 dinggai,0 dianguangzhi ") 
		.append("           from ic_general_h h, ic_general_b b, bd_rdcl rd, bd_cubasdoc cub, bd_cumandoc cum ") 
		.append("          where h.cgeneralhid = b.cgeneralhid ") 
		.append("            and h.cdispatcherid = rd.pk_rdcl ") 
		.append("            and h.ccustomerid = cum.pk_cumandoc ") 
		.append("            and cub.pk_cubasdoc = cum.pk_cubasdoc ") 
		.append("            and rd.rdcode in ('0601') ") 
		.append("            and rd.pk_corp = '"+pk_corp+"' ") ;
		if(wheres.get("dbilldatestart")==null || wheres.get("dbilldatestart").equals(null)){
			sql.append("            and h.dbilldate >= '' or ''  is null ") ; 
		}else{
			sql.append("            and h.dbilldate >= '"+wheres.get("dbilldatestart")+"' or '"+wheres.get("dbilldatestart")+"'  is null ") ;
		}
		if(wheres.get("dbilldateend")==null || wheres.get("dbilldateend").equals(null)){
			sql.append("            and h.dbilldate <= '' or ''  is null ") ; 
		}else{
			sql.append("            and h.dbilldate <= '"+wheres.get("dbilldateend")+"' or '"+wheres.get("dbilldateend")+"'  is null ") ;
		}
		sql.append("            and h.pk_corp = '"+pk_corp+"' ") 
		.append("            and nvl(b.dr,0) = 0 ") 
		.append("            and nvl(h.dr,0) = 0 ") 
		//.append("            and b.noutnum > 0 ") //可能会有退货，为负数会sum剪掉
		.append("            group by cub.custname,cub.pk_cubasdoc)) ") ;
		if(wheres.get("pk_cubasdoc")==null){
			sql.append("            where pk_cubasdoc = ''  or  '' is null  ") ;
		}else{
			sql.append("            where pk_cubasdoc = '"+wheres.get("pk_cubasdoc")+"'  or  '"+wheres.get("pk_cubasdoc")+"' is null  ") ; 
		}
		sql.append("            group by custname,pk_cubasdoc ") ;

		IUAPQueryBS uap1 =  (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//本期订单数
		MapListProcessor blps = new MapListProcessor();
		Object obj = null;
		obj = uap1.executeQuery(sql.toString(),blps);
		ArrayList addrList = (ArrayList) obj;
		//根据查询近期的销售订单，得到，该客户最近期的出库的第多数量.
		HashMap<String, UFDouble> salenum = querySaleNum(addrList);
		
		DueReportVO[] bbvo = new DueReportVO[addrList.size()];
		DecimalFormat df = new DecimalFormat("#.00");
		DecimalFormat df4 = new DecimalFormat("#.0000");
		if (addrList != null && addrList.size() > 0) {
			for (int i = 0; i < addrList.size(); i++) {
				Map addrMap = (Map) addrList.get(i);
				DueReportVO bdvo = new DueReportVO();
				String pk_cubasdoc = addrMap.get("pk_cubasdoc").toString();
				String custname = addrMap.get("custname").toString();
				UFDouble vdef0 = new UFDouble(addrMap.get("vdef0").toString());
				//垫罐纸套转吨--start--累计发出的垫罐纸
				UFDouble everyNum = salenum.get(pk_cubasdoc);
				//3  (8558/389+1)/1500
				UFDouble vdef0dun = new UFDouble(0);
				if(!vdef0.equals(0)){
					if(everyNum != null){
						UFDouble evezh = everyNum.div(new UFDouble(389)).add(new UFDouble(1));
						UFDouble sumzh = evezh.multiply(vdef0);
						vdef0dun = sumzh.div(new UFDouble(1500));
					}else{
						everyNum = new UFDouble(8169);//防止报错，如果当前用户没有拿到销售订单值，则给当前用户默认每垛数量8169--
						UFDouble evezh = everyNum.div(new UFDouble(389)).add(new UFDouble(1));
						UFDouble sumzh = evezh.multiply(vdef0);
						vdef0dun = sumzh.div(new UFDouble(1500));
					}
				}
				//垫罐纸套转吨--end--
				UFDouble vdef1 = new UFDouble(addrMap.get("vdef1").toString());
				UFDouble vdef2 = new UFDouble(addrMap.get("vdef2").toString());
				UFDouble vdef3 = new UFDouble(addrMap.get("vdef3").toString());
				bdvo.setPk_cubasdoc(pk_cubasdoc);//
				bdvo.setCustname(custname);
				bdvo.setVdef0(vdef0);
				//回收
				bdvo.setVdef1(vdef1);
				bdvo.setVdef2(vdef2);
				bdvo.setVdef3(vdef3);
				//未回收 
		        String vdef4 = df.format(vdef0.sub(vdef1));
		        String vdef5 = df.format(vdef0.sub(vdef2));
				bdvo.setVdef4(new UFDouble(vdef4));
				bdvo.setVdef5(new UFDouble(vdef5));
				String vdef6 = df4.format(vdef0dun.sub(vdef3));
				bdvo.setVdef6(new UFDouble(vdef6));
				//回收率
				if(!vdef1.equals(0)){
					if(vdef0.equals(0)){
						String vdef7 = df.format(vdef1.multiply(new UFDouble(100)));
						bdvo.setVdef7(vdef7+"%");
					}else{
						String vdef7 = df.format(vdef1.div(vdef0).multiply(new UFDouble(100)));
						bdvo.setVdef7(vdef7+"%");
					}
				}else{
					bdvo.setVdef7("0.00%");
				}
				if(!vdef2.equals(0)){
					if(vdef0.equals(0)){
						String vdef8 = df.format(vdef2.multiply(new UFDouble(100)));
						bdvo.setVdef8(vdef8+"%");
					}else{
						String vdef8 = df.format(vdef2.div(vdef0).multiply(new UFDouble(100)));
						bdvo.setVdef8(vdef8+"%");
					}
				}else{
					bdvo.setVdef8("0.00%");
				}
				
				if(!vdef3.equals(0)){
					if(vdef0.equals(0)){
						String vdef9 = df.format(vdef3.multiply(new UFDouble(100)));
						bdvo.setVdef9(vdef9+"%");
					}else{
						String vdef9 = df.format(vdef3.div(vdef0dun).multiply(new UFDouble(100)));
						bdvo.setVdef9(vdef9+"%");
					}
				}else{
					bdvo.setVdef9("0.00%");
				}
				
				bbvo[i]=bdvo;
			}
		}
		
		this.getReportBase().setBodyDataVO(bbvo);//设值
		System.out.println("查询完成--SK");

    }
	private HashMap<String, UFDouble> querySaleNum(ArrayList addrList) {
		StringBuffer wheres = new StringBuffer();
		if (addrList != null && addrList.size() > 0) {
			for (int i = 0; i < addrList.size(); i++) {
				Map addrMap = (Map) addrList.get(i);
				String pk_cubasdoc = addrMap.get("pk_cubasdoc").toString();
				wheres.append("'"+pk_cubasdoc+"'");
				if(i<addrList.size()-1){
					wheres.append(",");
				}
			}
		}
		String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct cub.pk_cubasdoc,so.vdef11 from so_sale so ") 
		.append("        left join bd_cumandoc cum  ") 
		.append("        on so.ccustomerid = cum.pk_cumandoc ") 
		.append("        left join bd_cubasdoc cub ") 
		.append("        on cum.pk_cubasdoc = cub.pk_cubasdoc ") 
		.append("        where so.vdef11 > 0  ") 
		.append("        and nvl(so.dr,0)=0  ") 
		.append("        and so.pk_corp = '"+pk_corp+"'  ") 
		.append("        and  cub.pk_cubasdoc in ("+wheres.toString()+") ") ;
		IUAPQueryBS query =  (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//本期订单数
		MapListProcessor blps = new MapListProcessor();
		ArrayList arlist = null;
		try {
			arlist = (ArrayList) query.executeQuery(sql.toString(),blps);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, UFDouble> oklist = new HashMap<String, UFDouble>();
		if (arlist != null && arlist.size() > 0) {
			for (int i = 0; i < arlist.size(); i++) {
				Map addrMap = (Map) arlist.get(i);
				String pk_cubasdoc = addrMap.get("pk_cubasdoc").toString();
				UFDouble num = new UFDouble(addrMap.get("vdef11").toString());
				if(!oklist.containsKey(pk_cubasdoc)){
					oklist.put(pk_cubasdoc, num);
				}
			}
		}
		return oklist;
	}
	
	/**
	 * 取交集月
	 * */
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
	
}
