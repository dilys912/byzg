/**
 * 
 */
package nc.ui.so.so001.order;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author zwx 2014-9-1
 * @功能:获取Execel数据
 */
public class WriteToExcel {
	public static Workbook w = null;
	public static int rows = 0;
	public static SaleOrderVO[] ordervo = null;
	public static String pk_corp = "";
	public static String operator = "";

	/**
	 * 功能: 打开创建文件
	 */
	public static void creatFile(String sourceFile) {
		try {
			/** 创建只读的Excel工作薄的对象 */
			w = Workbook.getWorkbook(new File(sourceFile));
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

		List<SaleOrderVO> hbvolist = new ArrayList<SaleOrderVO>();
		Sheet ws = w.getSheet(sheetNum);
		rows = ws.getRows();// 行数

		pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();// 公司
		operator = ClientEnvironment.getInstance().getUser().getUserCode();
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		String cbiztypeSql = "select distinct businame,pk_busitype,busicode from bd_busitype where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'"; // 业务类型
		ArrayList<HashMap<String, String>> list1 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cbiztypeSql, new MapListProcessor());
		HashMap map1 = new HashMap();
		if (list1.size() > 0 && list1 != null) {
			for (int j = 0; j < list1.size(); j++) {
				String key = list1.get(j).get("busicode");
				String value = list1.get(j).get("pk_busitype");
				map1.put(key, value);
			}
		}

//		String ccustomerSql = "select distinct cub.custname,cub.custcode,cum.pk_cumandoc "
//				+ " from bd_cubasdoc cub "
//				+ " inner join bd_cumandoc cum"
//				+ "  on cub.pk_cubasdoc = cum.pk_cubasdoc"
//				+ " where nvl(cub.dr, 0) = 0"
//				+ " and nvl(cum.dr, 0) = 0"
//				+ " and custflag='2'" ;// 客商管理档案ID、收货单位客商管理档案ID、开票单位客商管理档案ID、收货单位  edit 1改为2
//				+ " and cum.pk_corp = '" + pk_corp + "'"; edit
		StringBuffer ccustomerSql = new StringBuffer();
		ccustomerSql.append(" select distinct bd_cubasdoc.custcode, ") 
		.append("                 bd_cubasdoc.custname, ") 
		.append("                 bd_cumandoc.pk_cumandoc, ") 
		.append("                 bd_cubasdoc.pk_cubasdoc ") 
		.append("   from bd_cumandoc, bd_cubasdoc ") 
		.append("  where bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ") 
		.append("    and ((bd_cumandoc.pk_corp = '" + pk_corp + "' AND ") 
		.append("        (bd_cumandoc.custflag = '0' OR bd_cumandoc.custflag = '2') and ") 
		.append("        (bd_cumandoc.frozenflag = 'N' or bd_cumandoc.frozenflag is null) and ") 
		.append("        (bd_cumandoc.sealflag is null or bd_cumandoc.sealflag = 'N')) and ") 
		.append("        (bd_cumandoc.sealflag is null AND ") 
		.append("        (frozenflag = 'N' or frozenflag is null))) ") 
		.append("  order by bd_cubasdoc.custcode  ") ;
		ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(ccustomerSql.toString(), new MapListProcessor());
		HashMap map2 = new HashMap();
		if (list2.size() > 0 && list2 != null) {
			for (int j = 0; j < list2.size(); j++) {
				String key = list2.get(j).get("custcode");
				String value = list2.get(j).get("pk_cumandoc");
				map2.put(key, value);
			}
		}

		String ctermprotocolidSql = "select distinct termname,pk_payterm from bd_payterm where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 收款协议
		ArrayList<HashMap<String, String>> list3 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(ctermprotocolidSql, new MapListProcessor());
		HashMap map3 = new HashMap();
		if (list3.size() > 0 && list3 != null) {
			for (int j = 0; j < list3.size(); j++) {
				String key = list3.get(j).get("termname");
				String value = list3.get(j).get("pk_payterm");
				map3.put(key, value);
			}
		}

		String ccustbankidSql = "select distinct accname,pk_custbank  from bd_custbank where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 开户银行档案ID
		ArrayList<HashMap<String, String>> list4 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(ccustbankidSql, new MapListProcessor());
		HashMap map4 = new HashMap();
		if (list4.size() > 0 && list4 != null) {
			for (int j = 0; j < list4.size(); j++) {
				String key = list4.get(j).get("accname");
				String value = list4.get(j).get("pk_custbank");
				map4.put(key, value);
			}
		}

		String cdeptidSql = "select distinct deptname,pk_deptdoc,deptcode from bd_deptdoc where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 部门档案ID
		ArrayList<HashMap<String, String>> list5 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cdeptidSql, new MapListProcessor());
		HashMap map5 = new HashMap();
		if (list5.size() > 0 && list5 != null) {
			for (int j = 0; j < list5.size(); j++) {
				String key = list5.get(j).get("deptcode");
				String value = list5.get(j).get("pk_deptdoc");
				map5.put(key, value);
			}
		}

		String cemployeeidSql = "select distinct psnname,pk_psndoc,psncode from bd_psndoc where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 业务员档案ID
		ArrayList<HashMap<String, String>> list6 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cemployeeidSql, new MapListProcessor());
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
				+ " inv.invcode, inv.invname,inv.pk_invbasdoc,invc.def18"
				+ " from bd_invmandoc invc inner join bd_invbasdoc inv"
				+ " on inv.pk_invbasdoc = invc.pk_invbasdoc"
				+ " where nvl(invc.dr, 0) = 0 and invc.pk_corp = '" + pk_corp
				+ "'" + " and nvl(inv.dr, 0) = 0  ";// 存货管理档案主键、存货基本档案主键

		ArrayList<HashMap<String, String>> list9 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cinventoryidSql, new MapListProcessor());
		HashMap map8 = new HashMap();
		HashMap map9 = new HashMap();
		HashMap map10=new HashMap();
		if (list9.size() > 0 && list9 != null) {
			for (int j = 0; j < list9.size(); j++) {
				String key = list9.get(j).get("invcode");
				String value = list9.get(j).get("pk_invmandoc");
				String value1 = list9.get(j).get("pk_invbasdoc");
				String value2=list9.get(j).get("def18");
				map9.put(key, value);
				map8.put(key, value1);
				map10.put(key, value2);
			}
		}
		String cbodywarehouseidSql = "select distinct pk_stordoc,storcode,storname from bd_stordoc where nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 附表仓库
		ArrayList<HashMap<String, String>> list12 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(cbodywarehouseidSql, new MapListProcessor());
		HashMap map12 = new HashMap();
		if (list12.size() > 0 && list12 != null) {
			for (int j = 0; j < list12.size(); j++) {
				String key = list12.get(j).get("storcode");
				String value = list12.get(j).get("pk_stordoc");
				map12.put(key, value);
			}
		}
		
		String ccurrencytypeidSql = "select distinct currtypename,pk_currtype,currtypecode from bd_currtype";// 原币
		ArrayList<HashMap<String, String>> list13 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(ccurrencytypeidSql, new MapListProcessor());
		HashMap map13 = new HashMap();
		if (list13.size() > 0 && list13 != null) {
			for (int j = 0; j < list13.size(); j++) {
				String key = list13.get(j).get("currtypecode");
				String value = list13.get(j).get("pk_currtype");
				map13.put(key, value);
			}
		}

		String csalecorpidSql = "select vsalestruname,csalestruid,pk_corp from bd_salestru";// 销售组织ID
		ArrayList<HashMap<String, String>> list15 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(csalecorpidSql, new MapListProcessor());
		HashMap map15 = new HashMap();
		if (list15.size() > 0 && list15 != null) {
			for (int j = 0; j < list15.size(); j++) {
				String key = list15.get(j).get("vsalestruname");
				String value = list15.get(j).get("csalestruid");
				map15.put(key, value);
			}
		}

		String ccalbodyidSql = "select bodyname,pk_calbody from bd_calbody where pk_corp ='"
				+ pk_corp + "'";// 库存组织ID、建议发货库存组织
		ArrayList<HashMap<String, String>> list16 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(ccalbodyidSql, new MapListProcessor());
		HashMap map16 = new HashMap();
		if (list16.size() > 0 && list16 != null) {
			for (int j = 0; j < list16.size(); j++) {
				String key = list16.get(j).get("bodyname");
				String value = list16.get(j).get("pk_calbody");
				map16.put(key, value);
			}
		}

		String vreceiptcodeSql = "select vreceiptcode from so_sale where  pk_corp ='"
				+ pk_corp + "'";// 单据号
		ArrayList<HashMap<String, String>> list17 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(vreceiptcodeSql, new MapListProcessor());
		HashMap map17 = new HashMap();
		if (list17.size() > 0 && list17 != null) {
			for (int j = 0; j < list17.size(); j++) {
				String key = list17.get(j).get("vreceiptcode");
				String value = list17.get(j).get("vreceiptcode");
				map17.put(key, value);
			}
		}

		String invcontrastdocSql = "select invcode,oldinvcode from bd_invcontrastdoc where  nvl(dr,0)=0 and pk_corp='"
				+ pk_corp + "'";// 存货对照
		ArrayList<HashMap<String, String>> list18 = (ArrayList<HashMap<String, String>>) iquery
				.executeQuery(invcontrastdocSql, new MapListProcessor());
		HashMap map18 = new HashMap();
		if (list18.size() > 0 && list18 != null) {
			for (int j = 0; j < list18.size(); j++) {
				String key = list18.get(j).get("oldinvcode");
				String value = list18.get(j).get("invcode");
				map18.put(key, value);
			}
		}
		
		for (int i = 4; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			SaleorderBVO[] bodyvo = new SaleorderBVO[1];
			SaleOrderVO hbvo = new SaleOrderVO();// 聚合VO

			// 表头数据
			SaleorderHVO hvo = new SaleorderHVO();// 存放Excel中数据
			StringBuffer info = new StringBuffer();// 存放错误信息
			String companyId=getString(cells[0].getContents());//公司主键
			if(!companyId.equals(pk_corp)){
				info.append("Excel中【公司PK】与您当前登录公司PK:"+pk_corp+"不一致！");
			}
			String vreceiptcode = getString(cells[1].getContents());// 单据号
			if (!getString(map17.get(vreceiptcode)).equals("")
					&&(getString(map17.get(vreceiptcode)) != null)) {
				info.append("【单据号】“" + vreceiptcode + "”已存在！" + "\n");
			}
			String cbiztype = getString(cells[3].getContents());// 业务类型
			if (getString(map1.get(cbiztype)).equals("")
					|| getString(map1.get(cbiztype)) == null) {
				info.append("【业务类型】“" + cbiztype + "”" + "\n");
			} else {
				cbiztype = map1.get(cbiztype).toString();
			}

			String dbilldate = getString(cells[5].getContents());// 单据日期
			if (dbilldate == null || dbilldate.equals("")) {
				dbilldate = getCurDate();
			}
			String ccustomerid = getString(cells[8].getContents());// 客商管理档案ID
			if (getString(map2.get(ccustomerid)).equals("")
					|| getString(map2.get(ccustomerid)) == null) {
				info.append("【客商管理档案ID】“" + ccustomerid + "”" + "\n");
			} else {
				ccustomerid = map2.get(ccustomerid).toString();
			}

			String ctermprotocolid = getString(cells[9].getContents());// 收款协议ID
			if (ctermprotocolid != null && !ctermprotocolid.equals("")) {
				if (getString(map3.get(ctermprotocolid)).equals("")
						|| getString(map3.get(ctermprotocolid)) == null) {
					info.append("【收款协议ID】“" + ctermprotocolid + "”" + "\n");
				} else {
					ctermprotocolid = map3.get(ctermprotocolid).toString();
				}
			}
			String creceiptcustomerid = getString(cells[10].getContents());// 收货单位客商管理档案ID
			if (getString(map2.get(creceiptcustomerid)).equals("")
					|| getString(map2.get(creceiptcustomerid)) == null) {
				info.append("【收货单位客商管理档案ID】“" + creceiptcustomerid + "”"
								+ "\n");
			} else {
				creceiptcustomerid = map2.get(creceiptcustomerid).toString();
			}

			String vreceiveaddress = getString(cells[11].getContents());// 收货地址

			String creceiptcorpid = getString(cells[12].getContents());// 开票单位客商管理档案ID
			if (getString(map2.get(creceiptcorpid)).equals("")
					|| getString(map2.get(creceiptcorpid)) == null) {
				info.append("【开票单位客商管理档案ID】“" + creceiptcorpid + "”" + "\n");
			} else {
				creceiptcorpid = map2.get(creceiptcorpid).toString();
			}

			String ccustbankid = getString(cells[13].getContents());// 开户银行档案ID
			if (ccustbankid != null && !ccustbankid.equals("")) {
				if (getString(map4.get(ccustbankid)).equals("")
						|| getString(map4.get(ccustbankid)) == null) {
					info.append("【开户银行档案ID】“" + ccustbankid + "”" + "\n");
				} else {
					ccustbankid = map4.get(ccustbankid).toString();
				}
			}
			String cdeptid = getString(cells[14].getContents());// 部门档案ID
			if (getString(map5.get(cdeptid)).equals("")
					|| getString(map5.get(cdeptid)) == null) {
				info.append("【部门档案ID】“" + cdeptid + "”" + "\n");
			} else {
				cdeptid = map5.get(cdeptid).toString();
			}
			String cemployeeid = getString(cells[15].getContents());// 业务员档案ID
			if (cemployeeid != null && !cemployeeid.equals("")) {
				if (getString(map6.get(cemployeeid)).equals("")
						|| getString(map6.get(cemployeeid)) == null) {
					info.append("【业务员档案ID】“" + cemployeeid + "”" + "\n");
				} else {
					cemployeeid = map6.get(cemployeeid).toString();
				}
			}
			String coperatorid = getString(cells[16].getContents());// 制单人
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
			String ndiscountrate = getString(cells[17].getContents());// 整单折扣
			String dmakedate = getString(cells[20].getContents());// 制单日期
			if (dmakedate == null || dmakedate.equals("")) {
				dmakedate = getCurDate();
			}
			String vnote = getString(cells[22].getContents());// 备注
			
			

			String cinventoryid = getString(cells[25].getContents());// 存货管理档案主键
			
			String vdef11=getString(cells[70].getContents());//整垛数量
			if (vdef11.equals("")|| vdef11 == null) {
				vdef11=getString(map10.get(cinventoryid));
			}
			String vdef8=getString(cells[71].getContents());//交货方式
			
			// 可变字段
			hvo.setVreceiptcode(vreceiptcode);
			hvo.setCbiztype(cbiztype);
			hvo.setDbilldate(Toolkits.getUFDate(dbilldate));
			hvo.setCcustomerid(ccustomerid);
			hvo.setCtermprotocolid(ctermprotocolid);
			hvo.setCreceiptcustomerid(creceiptcustomerid);
			hvo.setVreceiveaddress(vreceiveaddress);
			hvo.setCreceiptcorpid(creceiptcorpid);
		//	hvo.setCcustbankid(ccustbankid);
			hvo.setCdeptid(cdeptid);
			hvo.setCemployeeid(cemployeeid);
			hvo.setCoperatorid(coperatorid);
			hvo.setNdiscountrate(Toolkits.getUFDouble(ndiscountrate));
			hvo.setDmakedate(Toolkits.getUFDate(dmakedate));
			hvo.setVnote(vnote);
			hvo.setVdef8(vdef8);
  			hvo.setVdef11(vdef11); 


			// 固定字段
			hvo.setPk_corp(pk_corp);// 公司PK
			hvo.setCreceipttype("30");// 单据类型
			hvo.setBinitflag(UFBoolean.FALSE);// 期初标志
			String csalecorpid = getString(cells[6].getContents());// 销售组织
			if (getString(map15.get(csalecorpid)).equals("")
					|| getString(map15.get(csalecorpid)) == null) {
				info.append("【 销售组织】“" + csalecorpid + "”" + "\n");
			} else {
				hvo.setCsalecorpid(map15.get(csalecorpid).toString());
			}
			String ccalbodyid = getString(cells[7].getContents());// 库存组织
			if (getString(map16.get(ccalbodyid)).equals("")
					|| getString(map16.get(ccalbodyid)) == null) {
				info.append("【 库存组织】“" + ccalbodyid + "”" + "\n");
			} else {
				hvo.setCcalbodyid(map16.get(ccalbodyid).toString());
			}
			hvo.setBfreecustflag(UFBoolean.FALSE);// 是否散户
			hvo.setIbalanceflag(0);// 结算标志
			hvo.setFstatus(1);// 状态

			hvo.setBretinvflag(UFBoolean.FALSE);// 退货标记
			hvo.setBoutendflag(UFBoolean.FALSE);// 出库结束标记
			hvo.setBinvoicendflag(UFBoolean.FALSE);// 开票结束标记
			hvo.setBreceiptendflag(UFBoolean.FALSE);// 发货结束标记
			hvo.setBpayendflag(UFBoolean.FALSE);// 付款结束标记
			hvo.setStatus(2);// '2'为新增

			hvo.setBdeliver(UFBoolean.TRUE);//是否发运
			hbvo.setParentVO(hvo);

			// 表体数据
			String cinvbasdocid = getString(cells[24].getContents());// 存货基本档案主键
			
			if (getString(map18.get(cinvbasdocid)).equals("")
					|| getString(map18.get(cinvbasdocid)) == null) {
				if (getString(map8.get(cinvbasdocid)).equals("")
						|| getString(map8.get(cinvbasdocid)) == null) {
					info.append("【存货基本档案主键】“" + cinvbasdocid + "”找不到对应主键" + "\n");
				} else {
					cinvbasdocid = map8.get(cinvbasdocid).toString();
				}
			}else{
				String basCode = getString(map18.get(cinvbasdocid));  //主键在存货对照中oldinvcode有值则判断存货基本档案是否有对应主键
				if (getString(map8.get(basCode)).equals("")
						|| getString(map8.get(basCode)) == null) {
					info.append("【存货基本档案主键】“" + basCode + "”找不到对应主键" + "\n");
				} else {
					cinvbasdocid = map8.get(basCode).toString();
				}
			}

			
			if (getString(map18.get(cinventoryid)).equals("")
					|| getString(map18.get(cinventoryid)) == null) {
					if (getString(map9.get(cinventoryid)).equals("")
							|| getString(map9.get(cinventoryid)) == null) {
						info.append("【存货管理档案主键】“" + cinventoryid + "”找不到对应主键" + "\n");
					} else {
						cinventoryid = map9.get(cinventoryid).toString();
					}
			} else {
				String manCode = getString(map18.get(cinventoryid));
				if (getString(map9.get(manCode)).equals("")
						|| getString(map9.get(manCode)) == null) {
					info.append("【存货管理档案主键】“" + manCode + "”找不到对应主键" + "\n");
				} else {
					cinventoryid = map9.get(manCode).toString();
				}
			}

			
			
			String nnumber = getString(cells[26].getContents());// 数量
			String nitemdiscountrate = getString(cells[27].getContents());// 单品折扣率
			String nexchangeotoarate = getString(cells[30].getContents());// 折辅汇率
			String noriginalcurprice = getString(cells[33].getContents());// 原币无税单价
			String noriginalcurtaxprice = getString(cells[34].getContents());// 原币含税单价
			String noriginalcurnetprice = getString(cells[35].getContents());// 原币无税净价
			String noriginalcurtaxnetprice = getString(cells[36].getContents());// 原币含税净价
			String noriginalcurtaxmny = getString(cells[37].getContents());// 原币税额
			String noriginalcurmny = getString(cells[38].getContents());// 原币无税金额
			String noriginalcursummny = getString(cells[39].getContents());// 原币价税合计
			String noriginalcurdiscountmny = getString(cells[40].getContents());// 原币折扣额
			String nprice = getString(cells[41].getContents());// 本币无税单价
			String ntaxprice = getString(cells[42].getContents());// 本币含税单价
			String nnetprice = getString(cells[43].getContents());// 本币无税净
			String ntaxnetprice = getString(cells[44].getContents());// 本币含税净价
			String ntaxmny = getString(cells[45].getContents());// 本币税额
			String nmny = getString(cells[46].getContents());// 本币无税金额
			String nsummny = getString(cells[47].getContents());// 本币价税合计
			String ndiscountmny = getString(cells[48].getContents());// 本币折扣额
			String blargessflag = getString(cells[49].getContents());// 是否赠品
			String frownote = getString(cells[51].getContents());// 行备注
			String coperatorid6 = getString(cells[52].getContents());// 制单人
			if (coperatorid6 == null || coperatorid6.equals("")) {
				coperatorid6 = map7.get(operator).toString();
			} else {
				if (getString(map7.get(coperatorid6)).equals("")
						|| getString(map7.get(coperatorid6)) == null) {
					info.append("【制单人】“" + coperatorid6 + "”" + "\n");
				} else {
					coperatorid6 = map7.get(coperatorid6).toString();
				}
			}
			String cbodywarehouseid = getString(cells[53].getContents());// 附表仓库
			if (cbodywarehouseid != null && !cbodywarehouseid.equals("")) {
				if (getString(map12.get(cbodywarehouseid)).equals("")
						|| getString(map12.get(cbodywarehouseid)) == null) {
					info.append("【 附表仓库】“" + cbodywarehouseid + "”" + "\n");
				} else {
					cbodywarehouseid = map12.get(cbodywarehouseid).toString();
				}
			}
			String dconsigndate = getString(cells[54].getContents());// 发货日期
			String ddeliverdate = getString(cells[55].getContents());// 交货日期
			String vreceiveaddress8 = getString(cells[62].getContents());// 收货地址
			String creceiptcorpid9 = getString(cells[63].getContents());// 收货单位
			if (getString(map2.get(creceiptcorpid9)).equals("")
					|| getString(map2.get(creceiptcorpid9)) == null) {
				info.append("【收货单位】“" + creceiptcorpid9 + "”" + "\n");
			} else {
				creceiptcorpid9 = map2.get(creceiptcorpid9).toString();
			}

			// Excel末尾新增字段
			String nquoteunitnum = getString(cells[64].getContents());// 报价计量数量
			String norgqttaxnetprc = getString(cells[65].getContents());// 报价单位含税净价
			String norgqtnetprc = getString(cells[66].getContents());// 报价单位无税净价
			String norgqttaxprc=getString(cells[67].getContents());//报价单位含税单价
			String norgqtprc=getString(cells[68].getContents());//报价单位无税单价


			// 可变字段
			int m = 0;
			bodyvo[m] = new SaleorderBVO();
			bodyvo[m].setCinvbasdocid(cinvbasdocid);
			bodyvo[m].setCinventoryid(cinventoryid);
			bodyvo[m].setNnumber(Toolkits.getUFDouble(nnumber));
			bodyvo[m].setNitemdiscountrate(Toolkits
					.getUFDouble(nitemdiscountrate));
			bodyvo[m].setNexchangeotoarate(Toolkits
					.getUFDouble(nexchangeotoarate));
			bodyvo[m].setNoriginalcurprice(Toolkits
					.getUFDouble(noriginalcurprice));
			bodyvo[m].setNoriginalcurtaxprice(Toolkits
					.getUFDouble(noriginalcurtaxprice));
			bodyvo[m].setNoriginalcurnetprice(Toolkits
					.getUFDouble(noriginalcurnetprice));
			bodyvo[m].setNoriginalcurtaxnetprice(Toolkits
					.getUFDouble(noriginalcurtaxnetprice));
			bodyvo[m].setNoriginalcurtaxmny(Toolkits
					.getUFDouble(noriginalcurtaxmny));
			bodyvo[m].setNoriginalcurmny(Toolkits.getUFDouble(noriginalcurmny));
			bodyvo[m].setNoriginalcursummny(Toolkits
					.getUFDouble(noriginalcursummny));
			bodyvo[m].setNoriginalcurdiscountmny(Toolkits
					.getUFDouble(noriginalcurdiscountmny));
			bodyvo[m].setNprice(Toolkits.getUFDouble(nprice));
			bodyvo[m].setNtaxprice(Toolkits.getUFDouble(ntaxprice));
			bodyvo[m].setNnetprice(Toolkits.getUFDouble(nnetprice));
			bodyvo[m].setNtaxnetprice(Toolkits.getUFDouble(ntaxnetprice));
			bodyvo[m].setNtaxmny(Toolkits.getUFDouble(ntaxmny));
			bodyvo[m].setNmny(Toolkits.getUFDouble(nmny));
			bodyvo[m].setNsummny(Toolkits.getUFDouble(nsummny));
			bodyvo[m].setNdiscountmny(Toolkits.getUFDouble(ndiscountmny));
			bodyvo[m].setBlargessflag(UFBoolean.valueOf(blargessflag));
			bodyvo[m].setFrownote(frownote);
			bodyvo[m].setCoperatorid(coperatorid6);
			bodyvo[m].setCbodywarehouseid(cbodywarehouseid);
			bodyvo[m].setDconsigndate(Toolkits.getUFDate(dconsigndate));
			bodyvo[m].setDdeliverdate(Toolkits.getUFDate(ddeliverdate));
			bodyvo[m].setVreceiveaddress(vreceiveaddress8);
			bodyvo[m].setCreceiptcorpid(creceiptcorpid9);

			bodyvo[m].setNquoteunitnum(Toolkits.getUFDouble(nquoteunitnum));
			bodyvo[m].setNorgqttaxnetprc(Toolkits.getUFDouble(norgqttaxnetprc));
			bodyvo[m].setNorgqtnetprc(Toolkits.getUFDouble(norgqtnetprc));
			bodyvo[m].setNorgqttaxprc(Toolkits.getUFDouble(norgqttaxprc));
			UFDouble tsbl=Toolkits.getUFDouble(100).sub(Toolkits.getUFDouble(nitemdiscountrate));
			
			bodyvo[m].setTsbl(tsbl);
			bodyvo[m].setNorgqtprc(Toolkits.getUFDouble(norgqtprc));


			// 固定字段
			bodyvo[m].setPkcorp(pk_corp);// 公司主键
			bodyvo[m].setNdiscountrate(Toolkits.getUFDouble(100));// 整单折扣
			bodyvo[m].setNexchangeotobrate(Toolkits.getUFDouble(cells[29]
					.getContents().toString()));// 折本汇率
			bodyvo[m].setNtaxrate(Toolkits.getUFDouble(cells[31].getContents()
					.toString()));// 税率
			String ccurrencytypeid = getString(cells[32].getContents());// 原币
			if (getString(map13.get(ccurrencytypeid)).equals("")
					|| getString(map13.get(ccurrencytypeid)) == null) {
				info.append("【 原币】“" + ccurrencytypeid + "”" + "\n");
			} else {
				bodyvo[m].setCcurrencytypeid(map13.get(ccurrencytypeid)
						.toString());
			}
			bodyvo[m].setFrowstatus(1);// 行状态
			String cadvisecalbodyid = getString(cells[61].getContents());// 建议发货库存组织
			if (getString(map16.get(cadvisecalbodyid)).equals("")
					|| getString(map16.get(cadvisecalbodyid)) == null) {
				info.append("【 建议发货库存组织】“" + cadvisecalbodyid + "”" + "\n");
			} else {
				bodyvo[m].setCadvisecalbodyid(map16.get(cadvisecalbodyid)
						.toString());
			}
			bodyvo[m].setStatus(2);// '2'表示新增
			hbvo.setChildrenVO(bodyvo);
			hbvo.setStatus(2);// '2'表示新增
			hvo.setVdef20(info.toString());
			hbvolist.add(hbvo);
		}
		

		if (hbvolist != null && hbvolist.size() > 0) {
			ordervo = (SaleOrderVO[]) hbvolist.toArray(new SaleOrderVO[hbvolist
					.size()]);
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
