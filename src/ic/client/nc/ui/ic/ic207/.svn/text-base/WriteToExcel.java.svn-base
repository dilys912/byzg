package nc.ui.ic.ic207;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class WriteToExcel {
	public static Workbook wt = null;
	public static String pk_corp = "";
	public static int rows = 0;
	public static GeneralBillVO[] generalbillvo = null;
	public static String operator = "";
	
	public static void creatFile(String sourceFile) {
		try {
			/** 创建只读的Excel工作薄的对象 */
			wt = Workbook.getWorkbook(new File(sourceFile));
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 得到Excel数据
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings( { "unchecked" })
	public void readData(int sheetNum) throws BusinessException {

		List<GeneralBillVO> hbvolist = new ArrayList<GeneralBillVO>();
		Sheet ws = wt.getSheet(sheetNum);
		rows = ws.getRows();// 行数

		pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();// 公司
		operator = ClientEnvironment.getInstance().getUser().getUserCode();
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		
		
		String vbillcodeSql = "select vbillcode from ic_general_h where  pk_corp ='"+ pk_corp + "' and nvl(dr,0)=0";// 单据号
		ArrayList<HashMap<String, String>> list1 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(vbillcodeSql, new MapListProcessor());
		HashMap map1 = new HashMap();
		if (list1.size() > 0 && list1 != null) {
			for (int j = 0; j < list1.size(); j++) {
				String key = list1.get(j).get("vbillcode");
				String value = list1.get(j).get("vbillcode");
				map1.put(key, value);
			}
		}
		
		
		String cwarehouseidSql = "select distinct pk_stordoc,storcode,storname from bd_stordoc where nvl(dr,0)=0 and pk_corp='"
			+ pk_corp + "'";// 仓库
	    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) iquery
			     .executeQuery(cwarehouseidSql, new MapListProcessor());
	    HashMap map2 = new HashMap();
	    if (list2.size() > 0 && list2 != null) {
	 	     for (int j = 0; j < list2.size(); j++) {
		    	String key = list2.get(j).get("storcode");
			    String value = list2.get(j).get("pk_stordoc");
			    map2.put(key, value);
		     }
	    }
	
	
	    String pk_calbodySql = "select pk_calbody,bodycode, bodyname from bd_calbody where pk_corp ='"+ pk_corp + "'";// 库存组织ID
        ArrayList<HashMap<String, String>> list3 = (ArrayList<HashMap<String, String>>) iquery
		         .executeQuery(pk_calbodySql, new MapListProcessor());
        HashMap map3 = new HashMap();
        if (list3.size() > 0 && list3 != null) {
	        for (int j = 0; j < list3.size(); j++) {
		       String key = list3.get(j).get("bodyname");
		       String value = list3.get(j).get("pk_calbody");
		       map3.put(key, value);
	        }
        }
		
		
        String cdispatcheridSql = "select distinct pk_rdcl,rdcode,rdname from bd_rdcl where nvl(dr,0)=0 and pk_corp='"
	                             + pk_corp + "'"; // 收发类别
        ArrayList<HashMap<String, String>> list4 = (ArrayList<HashMap<String, String>>) iquery
	               .executeQuery(cdispatcheridSql, new MapListProcessor());
        HashMap map4 = new HashMap();
        if (list4.size() > 0 && list4 != null) {
             for (int j = 0; j < list4.size(); j++) {
	              String key = list4.get(j).get("rdcode");
	              String value = list4.get(j).get("pk_rdcl");
	              map4.put(key, value);
             }
         }	
		




		
		String cdptidSql = "select distinct deptname,pk_deptdoc,deptcode from bd_deptdoc where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 部门档案ID
		ArrayList<HashMap<String, String>> list5 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cdptidSql, new MapListProcessor());
		HashMap map5 = new HashMap();
		if (list5.size() > 0 && list5 != null) {
			for (int j = 0; j < list5.size(); j++) {
				String key = list5.get(j).get("deptcode");
				String value = list5.get(j).get("pk_deptdoc");
				map5.put(key, value);
			}
		}

		String cbizidSql = "select distinct psnname,pk_psndoc,psncode from bd_psndoc where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 业务员档案ID
		ArrayList<HashMap<String, String>> list6 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cbizidSql, new MapListProcessor());
		HashMap map6 = new HashMap();
		if (list6.size() > 0 && list6 != null) {
			for (int j = 0; j < list6.size(); j++) {
				String key = list6.get(j).get("psnname");
				String value = list6.get(j).get("pk_psndoc");
				map6.put(key, value);
			}
		}

		String coperatoridSql = "select distinct user_name,cuserid,user_code from sm_user where nvl(dr,0)=0";// 制单人
		ArrayList<HashMap<String, String>> list7 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(coperatoridSql, new MapListProcessor());
		HashMap map7 = new HashMap();
		if (list7.size() > 0 && list7 != null) {
			for (int j = 0; j < list7.size(); j++) {
				String key = list7.get(j).get("user_code");
				String value = list7.get(j).get("cuserid");
				map7.put(key, value);
			}
		}

		String cinventoryidSql = "select distinct invc.pk_invmandoc,"
				+ " inv.invcode, inv.invname,inv.pk_invbasdoc,invc.def18,invc.wholemanaflag,inv.free1"
				+ " from bd_invmandoc invc inner join bd_invbasdoc inv"
				+ " on inv.pk_invbasdoc = invc.pk_invbasdoc"
				+ " where nvl(invc.dr, 0) = 0 and invc.pk_corp = '" + pk_corp
				+ "'" + " and nvl(inv.dr, 0) = 0  ";// 存货管理档案主键、存货基本档案主键

		ArrayList<HashMap<String, String>> list9 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cinventoryidSql, new MapListProcessor());
		HashMap map8 = new HashMap();
		HashMap map9 = new HashMap();
		HashMap map10 = new HashMap();
		HashMap map11 = new HashMap();

		if (list9.size() > 0 && list9 != null) {
			for (int j = 0; j < list9.size(); j++) {
				String key = list9.get(j).get("invcode");
				String value = list9.get(j).get("pk_invmandoc");
				String value1 = list9.get(j).get("pk_invbasdoc");
				String value2 = list9.get(j).get("wholemanaflag");
				String value3 = list9.get(j).get("free1");

				map9.put(key, value);
				map8.put(key, value1);
				map10.put(key, value2);
				map11.put(key, value3);

			}
		}
		
		
		String free1Sql = "select vfree1 from ic_general_b where pk_corp='" + pk_corp+ "' and nvl(dr,0)=0";

	ArrayList<HashMap<String, String>> list10 = (ArrayList<HashMap<String, String>>) iquery
			.executeQuery(free1Sql, new MapListProcessor());
	HashMap map12 = new HashMap();
	if (list9.size() > 0 && list10 != null) {
		for (int j = 0; j < list10.size(); j++) {
			String key = list10.get(j).get("vfree1");
			String value = list10.get(j).get("vfree1");
			map12.put(key, value);
		}
	}
	
		
		for (int i = 2; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			GeneralBillItemVO[] bodyvo = new GeneralBillItemVO[1];
			GeneralBillVO hbvo = new GeneralBillVO();// 聚合VO

			// 表头数据
			GeneralBillHeaderVO hvo = new GeneralBillHeaderVO();// 存放Excel中数据
			StringBuffer info = new StringBuffer();// 存放错误信息
			String companyId=getString(cells[0].getContents());//公司主键
			if(!companyId.equals(pk_corp)){
				info.append("Excel中【公司PK】与您当前登录公司PK:"+pk_corp+"不一致！");
			}
			String vbillcode = getString(cells[1].getContents());// 单据号
			if (!getString(map1.get(vbillcode)).equals("")
					&&(getString(map1.get(vbillcode)) != null)) {
				info.append("【单据号】“" + vbillcode + "”已存在！" + "\n");
			}
			
			
			String dbilldate = getString(cells[2].getContents());// 单据日期
			if (dbilldate == null || dbilldate.equals("")) {
				dbilldate = getCurDate();
			}
			
	
			String cwarehouseid = getString(cells[3].getContents());// 仓库
			if (cwarehouseid != null && !cwarehouseid.equals("")) {
				if (getString(map2.get(cwarehouseid)).equals("")
						|| getString(map2.get(cwarehouseid)) == null) {
					info.append("【仓库】“" + cwarehouseid + "”" + "\n");
				} else {
					cwarehouseid = map2.get(cwarehouseid).toString();
				}
			}
			
			String pk_calbody = getString(cells[6].getContents());// 库存组织
			if (pk_calbody!= null && !pk_calbody.equals("")) {
				if (getString(map3.get(pk_calbody)).equals("")
						|| getString(map3.get(pk_calbody)) == null) {
					info.append("【库存组织】“" + pk_calbody + "”" + "\n");
				} else {
					pk_calbody = map3.get(pk_calbody).toString();
				}
			}
	
			
			String cdispatcherid = getString(cells[7].getContents());// 收发类别
			if (getString(map4.get(cdispatcherid)).equals("")
					|| getString(map4.get(cdispatcherid)) == null) {
				info.append("【收发类别ID】“" + cdispatcherid + "”" + "\n");
			} else {
				cdispatcherid = map4.get(cdispatcherid).toString();
			}
			
			
			String freplenishflag  = getString(cells[9].getContents());// 退货标志		

			
	

/*			String cdptid = getString(cells[14].getContents());// 部门档案ID
			if (getString(map5.get(cdptid)).equals("")
					|| getString(map5.get(cdptid)) == null) {
				info.append("【部门档案ID】“" + cdptid + "”" + "\n");
			} else {
				cdptid = map5.get(cdptid).toString();
			}
			
			String cbizid = getString(cells[15].getContents());// 业务员档案ID
			if (cbizid != null && !cbizid.equals("")) {
				if (getString(map6.get(cbizid)).equals("")
						|| getString(map6.get(cbizid)) == null) {
					info.append("【业务员档案ID】“" + cbizid + "”" + "\n");
				} else {
					cbizid = map6.get(cbizid).toString();
				}
			}*/
			String coperatorid = getString(cells[14].getContents());// 制单人
			if (coperatorid == null || coperatorid.equals("")) {
				coperatorid = map7.get(operator).toString();
			} else {
				if (getString(map7.get(coperatorid)).equals("")
						|| getString(map7.get(coperatorid)) == null) {
					info.append("【制单人】“" + coperatorid + "”" + "\n");
				} else {
					coperatorid = map7.get(coperatorid).toString();
				}
			}
			
/*			String tmaketime = getString(cells[20].getContents());// 制单日期
			if (tmaketime == null || tmaketime.equals("")) {
				tmaketime = getCurDate();
			}*/
			
		
		//	String vnote = getString(cells[22].getContents());// 备注

			

			
			// 可变字段
			hvo.setVbillcode(vbillcode);   //单据号
			hvo.setCwarehouseid(cwarehouseid);  //仓库
	//		hvo.setCbiztypeid(cbiztype);
			hvo.setDbilldate(Toolkits.getUFDate(dbilldate));  //单据日期
			hvo.setCdispatcherid(cdispatcherid);		//收发类型 	
			hvo.setPk_calbody(pk_calbody);		//库存组织
	//		hvo.setCdptid(cdptid);
	//		hvo.setCbizid(cbizid);			
			hvo.setCoperatorid(coperatorid);    //制单人
			hvo.setCoperatoridnow(coperatorid);    //制单人
	//		hvo.setTmaketime(Toolkits.getUFDate(tmaketime));  制单日期
	//		hvo.setVnote(vnote);
	//		hvo.setFreplenishflag(freplenishflag);




			// 固定字段
			hvo.setPk_corp(pk_corp);// 公司PK
			hvo.setCbilltypecode("4A");// 单据类型
			hvo.setFbillflag(2);       //单据状态			
			hvo.setStatus(2);// '2'为新增
			hbvo.setParentVO(hvo);

			// 表体数据
			String cinvbasid = getString(cells[10].getContents());// 存货基本档案主键
	    	String cinventoryid = getString(cells[10].getContents());// 存货管理档案主键
	    	String vbatchcode = getString(cells[11].getContents());// 批次号
	    	String vfree1 = getString(cells[13].getContents());// 自由项
	    	String ninnum = getString(cells[12].getContents());// 数量
	    	
			if (cinvbasid!= null && !cinvbasid.equals("")) {
				if (getString(map8.get(cinvbasid)).equals("")
						|| getString(map8.get(cinvbasid)) == null) {
					info.append("【存货基本档案主键】“" + cinvbasid + "”" + "\n");
				} else {
					cinvbasid = map8.get(cinvbasid).toString();
				}
			}
			
			if (cinventoryid!= null && !cinventoryid.equals("")) {
				if (getString(map9.get(cinventoryid)).equals("")
						|| getString(map9.get(cinventoryid)) == null) {
					info.append("【存货管理档案主键】“" + cinventoryid + "”" + "\n");
				} else {
					cinventoryid = map9.get(cinventoryid).toString();
				}
			}
			
			
			String wholemanaflag = getString(cells[10].getContents());// 存货基本档案主键
			if (vbatchcode!= null && !vbatchcode.equals("")) {
				if (!getString(map10.get(wholemanaflag)).equals("Y")) {
					info.append("【批次号】“" + vbatchcode + "”应为空！" + "\n");
				}/* else if (!getString(map10.get(wholemanaflag)).equals("Y")){
					info.append("【批次号】“" + vbatchcode + "”" + "\n");
				}*/
			}else{
				if (getString(map10.get(wholemanaflag)).equals("Y")) {
					info.append("【批次号】“" + vbatchcode + "”不应为空" + "\n");
				} /*else if (getString(map10.get(wholemanaflag)).equals("N")){
					vbatchcode = map10.get("").toString();
				}*/
				
			}
			
			String free1 = getString(cells[10].getContents());// 存货基本档案主键
			if (vfree1!= null && !vfree1.equals("")) {
				if (getString(map11.get(free1)).equals("")
						|| getString(map11.get(free1)) == null||!getString(map11.get(free1)).equals("0001A21000000004EIQW")) {
					info.append("【自由项】“" + vfree1 + "”应为空" + "\n");
				} /*else if(!getString(map11.get(free1)).equals("0001A21000000004EIQW")){
					info.append("【自由项】“" + vfree1 + "”" + "\n");
				}*/
			}else{
				if (getString(map11.get(free1)).equals("0001A21000000004EIQW")) {
					info.append("【自由项】“" + vfree1 + "”不应为空" + "\n");
				} /*else {
					vfree1 = map11.get("").toString();
				}*/
				
			}
			
	//		String vfree1 = getString(cells[13].getContents());// 自由项
			if (!getString(map12.get(vfree1)).equals("")
					&&(getString(map12.get(vfree1)) != null)) {
				info.append("【自由项】“" + vfree1 + "”已存在！" + "\n");
			}

			
			// 可变字段
			int m = 0;
			bodyvo[m] = new GeneralBillItemVO();
			bodyvo[m].setCinvbasid(cinvbasid);
			bodyvo[m].setCinventoryid(cinventoryid);
			bodyvo[m].setDbizdate(Toolkits.getUFDate(dbilldate)); 
			bodyvo[m].setCrowno("10");
			bodyvo[m].setVbatchcode(vbatchcode);
			bodyvo[m].setVfree1(vfree1);
			bodyvo[m].setNinnum(Toolkits.getUFDouble(ninnum));
			
			// 固定字段
			bodyvo[m].setStatus(2);// '2'表示新增
			hbvo.setChildrenVO(bodyvo);
			hbvo.setStatus(2);// '2'表示新增
			hvo.setVuserdef9(info.toString());
			hbvolist.add(hbvo);
		}
		

		if (hbvolist != null && hbvolist.size() > 0) {
			generalbillvo = (GeneralBillVO[]) hbvolist.toArray(new GeneralBillVO[hbvolist.size()]);
		}

	}
	
	
	public String getString(String obj) {

		if (obj != null) {
			try {
				return obj == null ? "" : obj.toString().trim();
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getString(Object obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : obj.toString().trim();
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getStringFromExe(Object obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : String.valueOf(obj).trim();
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}

	}
	
	/**
	 * 
	 * 返回当前字符串型日期
	 * 
	 * @return String 返回的字符串型日期
	 */
	public static String getCurDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = simpledateformat.format(calendar.getTime());
		return strDate;
	}


}
