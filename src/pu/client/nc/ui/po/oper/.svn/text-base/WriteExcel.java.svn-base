package nc.ui.po.oper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/*采购订单导入
 * ljy
 * 2014-09-22
 */
public class WriteExcel {
	public static Workbook w = null;
	public static int rows = 0;
	public static OrderVO[] vo_hb1;

	public static void creatFile(String sourceFile) {
		try {
			w = Workbook.getWorkbook(new File(sourceFile));
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public <object> void readData(int sheetNum) throws BusinessException {
		List list = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		new UFDate();
		StringBuffer info = new StringBuffer("");
		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();
		String user = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		Sheet ws = w.getSheet(sheetNum);
		rows = ws.getRows();
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		
		StringBuffer strwhere = new StringBuffer("");
		for (int i = 4; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			String cvendormangid = getString(cells[8].getContents());
			if (!"".equals(cvendormangid)&&strwhere.indexOf(cvendormangid)<0) {
				if (i==4) {
					strwhere.append(" '"+cvendormangid+"' ");
				}else{
					strwhere.append(" ,'"+cvendormangid+"' ");
				}
			}
		}
		
		String sql1 = " select distinct pk_busitype,busicode,businame from bd_busitype where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0 "; // 业务类型
		ArrayList<HashMap<String, String>> list1 = (ArrayList) iquery
				.executeQuery(sql1, new MapListProcessor());
		HashMap<String, String> map1 = new HashMap<String, String>();
		if ((list1.size() > 0) && (list1 != null)) {
			for (int j = 0; j < list1.size(); j++) {
				String key = list1.get(j).get("busicode");
				String value = list1.get(j).get("pk_busitype");
				map1.put(key, value);
			}
		}
		
//		String sql2 = "select distinct pk_custbank,account,accname from bd_custbank where nvl(dr,0)=0";// 银行名称
//		ArrayList<HashMap<String, String>> list2 = (ArrayList) iquery
//				.executeQuery(sql2, new MapListProcessor());
		HashMap<String, String> map2 = new HashMap<String, String>();
//		if ((list2.size() > 0) && (list2 != null)) {
//			for (int j = 0; j < list2.size(); j++) {
//				String key = list2.get(j).get("account");
//				String value = list2.get(j).get("pk_custbank");
//				map2.put(key, value);
//			}
//		}
		
		String sql3 = "select distinct pk_deptdoc,deptcode,deptname from bd_deptdoc where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0";// 采购部门名称
		ArrayList<HashMap<String, String>> list3 = (ArrayList) iquery
				.executeQuery(sql3, new MapListProcessor());
		HashMap<String, String> map3 = new HashMap<String, String>();
		if ((list3.size() > 0) && (list3 != null)) {
			for (int j = 0; j < list3.size(); j++) {
				String key = list3.get(j).get("deptcode");
				String value = list3.get(j).get("pk_deptdoc");
				map3.put(key, value);
			}
		}
		String sql4 = "select distinct pk_psndoc,psnname,psncode from bd_psndoc where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0";// 采购员名称
		ArrayList<HashMap<String, String>> list4 = (ArrayList) iquery
				.executeQuery(sql4, new MapListProcessor());
		HashMap<String, String> map4 = new HashMap<String, String>();
		if ((list4.size() > 0) && (list4 != null)) {
			for (int j = 0; j < list4.size(); j++) {
				String key = list4.get(j).get("psncode");
				String value = list4.get(j).get("pk_psndoc");
				map4.put(key, value);
			}
		}
		String sql5 = "select distinct cuserid,user_code,user_name  from sm_user  where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0";
		ArrayList<HashMap<String, String>> list5 = (ArrayList) iquery
				.executeQuery(sql5, new MapListProcessor());
		HashMap<String, String> map5 = new HashMap<String, String>();
		if ((list5.size() > 0) && (list5 != null)) {
			for (int j = 0; j < list5.size(); j++) {
				String key = list5.get(j).get("user_code");
				String value = list5.get(j).get("cuserid");
				map5.put(key, value);
			}
		}
//		String sql6 = "select distinct cum.pk_cumandoc,cub.pk_cubasdoc,cub.custcode, cub.custname from bd_cumandoc cum inner join bd_cubasdoc cub on cub.pk_cubasdoc = cum.pk_cubasdoc and nvl(cub.dr, 0) = 0 where nvl(cum.dr, 0) = 0  and custflag='1'";
		StringBuffer sql6 = new StringBuffer();
		sql6.append("  ") 
		.append(" select distinct bd_cubasdoc.custcode, ") 
		.append("                 bd_cubasdoc.custname, ") 
		.append("                 bd_cumandoc.pk_cumandoc, ") 
		.append("                 bd_cubasdoc.pk_cubasdoc, ") 
		.append("                 bd_custbank.pk_custbank ") 
		.append("   from bd_cumandoc ") 
		.append("  inner join bd_cubasdoc on bd_cumandoc.pk_cubasdoc = ") 
		.append("                            bd_cubasdoc.pk_cubasdoc ") 
		.append("   left join bd_custbank on bd_custbank.pk_cubasdoc = ") 
		.append("                            bd_cubasdoc.pk_cubasdoc ") 
		.append("                        and nvl(bd_custbank.defflag, 'N') = 'Y' ") 
		.append("                        and nvl(bd_custbank.dr, 0) = 0 ") 
		.append("  where (((bd_cumandoc.custflag = '1' or bd_cumandoc.custflag = '3') and ") 
		.append("        (bd_cumandoc.frozenflag = 'N' OR bd_cumandoc.frozenflag = 'n' OR ") 
		.append("        bd_cumandoc.frozenflag IS NULL) and bd_cumandoc.pk_corp = '"+pk_corp+"') and ") 
		.append("        (bd_cumandoc.sealflag is null AND ") 
		.append("        (bd_cumandoc.frozenflag = 'N' or bd_cumandoc.frozenflag is null))) and bd_cubasdoc.custcode in ("+strwhere+") ") ;
		// 供应商管理 供应商基本
		ArrayList<HashMap<String, String>> list6 = (ArrayList) iquery
				.executeQuery(sql6.toString(), new MapListProcessor());
		HashMap<String, String> map6 = new HashMap<String, String>();
		HashMap<String, String> map7 = new HashMap<String, String>();
		if ((list6.size() > 0) && (list6 != null)) {
			for (int j = 0; j < list6.size(); j++) {
				String key = list6.get(j).get("custcode");
				String value = list6.get(j).get("pk_cumandoc");// 供应商管理
				String value1 = list6.get(j).get("pk_cubasdoc");// 供应商基本
				String pk_custbank = list6.get(j).get("pk_custbank")==null?"":list6.get(j).get("pk_custbank");// 供应商银行账户
				map6.put(key, value);
				map7.put(key, value1);
				map2.put(key, pk_custbank);
			}
		}

		String sql8 = " select distinct invc.pk_invmandoc,inv.pk_invbasdoc, inv.invcode, inv.invname from bd_invmandoc invc inner join bd_invbasdoc inv on inv.pk_invbasdoc = invc.pk_invbasdoc   and nvl(inv.dr, 0) = 0 where invc.pk_corp ='"
				+ pk_corp + "' and nvl(invc.dr, 0) = 0";
		ArrayList<HashMap<String, String>> list8 = (ArrayList) iquery
				.executeQuery(sql8, new MapListProcessor());
		HashMap<String, String> map8 = new HashMap<String, String>();
		HashMap<String, String> map9 = new HashMap<String, String>();
		if ((list8.size() > 0) && (list8 != null)) {
			for (int j = 0; j < list8.size(); j++) {
				String key = list8.get(j).get("invcode");
				String value = list8.get(j).get("pk_invmandoc");// 存货管理
				String value1 = list8.get(j).get("pk_invbasdoc");// 存货基本
				map8.put(key, value);
				map9.put(key, value1);
			}
		}

		String sql10 = "select distinct pk_currtype,currtypecode,currtypename from bd_currtype";// 原币币种
		ArrayList<HashMap<String, String>> list10 = (ArrayList) iquery
				.executeQuery(sql10, new MapListProcessor());
		HashMap<String, String> map10 = new HashMap<String, String>();
		if ((list10.size() > 0) && (list10 != null)) {
			for (int j = 0; j < list10.size(); j++) {
				String key = list10.get(j).get("currtypecode");
				String value = list10.get(j).get("pk_currtype");
				map10.put(key, value);
			}
		}
		String sql11 = "select distinct pk_stordoc,storcode,storname from bd_stordoc where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0";// 收货仓库名称，可空
		ArrayList<HashMap<String, String>> list11 = (ArrayList) iquery
				.executeQuery(sql11, new MapListProcessor());
		HashMap<String, String> map11 = new HashMap<String, String>();
		if ((list11.size() > 0) && (list11 != null)) {
			for (int j = 0; j < list11.size(); j++) {
				String key = list11.get(j).get("storcode");
				String value = list11.get(j).get("pk_stordoc");
				map11.put(key, value);
			}
		}
		String sql12 = "select distinct pk_purorg,code,name  from bd_purorg ";// 采购组织编码
		ArrayList<HashMap<String, String>> list12 = (ArrayList) iquery
				.executeQuery(sql12, new MapListProcessor());
		HashMap<String, String> map12 = new HashMap<String, String>();
		if ((list12.size() > 0) && (list12 != null)) {
			for (int j = 0; j < list12.size(); j++) {
				String key = list12.get(j).get("code");
				String value = list12.get(j).get("pk_purorg");
				map12.put(key, value);
			}
		}
		String sql13 = "select distinct pk_calbody  from  bd_calbody where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0";
		ArrayList<HashMap<String, String>> list13 = (ArrayList) iquery
				.executeQuery(sql13, new MapListProcessor());
		HashMap<String, String> map13 = new HashMap<String, String>();
		if ((list13.size() > 0) && (list13 != null)) {
			for (int j = 0; j < list13.size(); j++) {
				String key = list13.get(j).get("pk_calbody");
				String value = list13.get(j).get("pk_calbody");
				map13.put(key, value);
			}
		}
		String sql14 = "select distinct vordercode   from  po_order where  pk_corp='"
				+ pk_corp + "' ";
		ArrayList<HashMap<String, String>> list14 = (ArrayList) iquery
				.executeQuery(sql14, new MapListProcessor());
		HashMap<String, String> map14 = new HashMap<String, String>();
		if ((list14.size() > 0) && (list14 != null)) {
			for (int j = 0; j < list14.size(); j++) {
				String key = list14.get(j).get("vordercode");
				String value = list14.get(j).get("vordercode");
				map14.put(key, value);
			}
		}
		String sql15 = "select distinct invcode, oldinvcode from bd_invcontrastdoc  where  nvl(dr,0)=0 and  pk_corp='"
				+ pk_corp + "' ";
		ArrayList<HashMap<String, String>> list15 = (ArrayList) iquery
				.executeQuery(sql15, new MapListProcessor());
		HashMap<String, String> map15 = new HashMap<String, String>();
		if ((list15.size() > 0) && (list15 != null)) {
			for (int j = 0; j < list15.size(); j++) {
				String key = list15.get(j).get("oldinvcode");
				String value = list15.get(j).get("invcode");
				map15.put(key, value);
			}
		}
		for (int i = 4; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			OrderItemVO[] vo_body1 = new OrderItemVO[1];
			OrderHeaderVO vo_head1 = new OrderHeaderVO();
			OrderVO vo_hd = new OrderVO();
			if ((cells != null) && (cells.length != 0)) {
				String vordercode = getString(cells[0].getContents());
				if ((getString((String) map14.get(vordercode)) != null)
						&& (!getString((String) map14.get(vordercode)).equals(
								""))) {
					info.append(" 第"+(i+1)+"行【订单号】" + vordercode + "重复！\n");
				}
				String cbiztype = getString(cells[1].getContents());
				if ((getString((String) map1.get(cbiztype)).equals(""))
						|| (getString((String) map1.get(cbiztype)) == null))
					info.append(" 第"+(i+1)+"行【业务类型	】" + cbiztype + "不存在！\n");
				else {
					cbiztype = ((String) map1.get(cbiztype)).toString();
				}
				String caccountbankid = getString(cells[8].getContents());
				if ((getString((String) map2.get(caccountbankid)).equals(""))|| (getString((String) map2.get(caccountbankid)) == null)){
//					info.append(" 第"+(i+1)+"行【银行】" + caccountbankid + "不存在！\n");
				}else {
					caccountbankid = ((String) map2.get(caccountbankid))
							.toString();
				}
				String caccountyear = getString(cells[3].getContents());
				String cdeptid = getString(cells[4].getContents());
				if ((getString((String) map3.get(cdeptid)).equals(""))
						|| (getString((String) map3.get(cdeptid)) == null))
					info.append(" 第"+(i+1)+"行【采购部门】" + cdeptid + "不存在！\n");
				else {
					cdeptid = ((String) map3.get(cdeptid)).toString();
				}
				String cemployeeid = getString(cells[5].getContents());
				if ((getString((String) map4.get(cemployeeid)).equals(""))
						|| (getString((String) map4.get(cemployeeid)) == null))
					info.append(" 第"+(i+1)+"行【采购员】" + cemployeeid + "不存在！\n");
				else {
					cemployeeid = ((String) map4.get(cemployeeid)).toString();
				}
				String coperator = getString(cells[6].getContents());

				if ((coperator != null) && (!coperator.equals(""))) {
					if ((getString((String) map5.get(coperator)).equals(""))
							|| (getString((String) map5.get(coperator)) == null))
						info.append(" 第"+(i+1)+"行【制单人】" + coperator + "不存在！\n");
					else
						coperator = ((String) map5.get(coperator)).toString();
				} else {
					coperator = user;
				}
				String cdeliveraddress = getString(cells[7].getContents());
				String cvendormangid = getString(cells[8].getContents());
				if ((getString((String) map6.get(cvendormangid)).equals(""))
						|| (getString((String) map6.get(cvendormangid)) == null))
					info.append(" 第"+(i+1)+"行【供应商管理】" + cvendormangid + "不存在！\n");
				else {
					cvendormangid = ((String) map6.get(cvendormangid))
							.toString();
				}
				String cvendorbaseid = getString(cells[9].getContents());

				if ((getString((String) map7.get(cvendorbaseid)).equals(""))
						|| (getString((String) map7.get(cvendorbaseid)) == null))
					info.append(" 第"+(i+1)+"行【供应商基本】" + cvendorbaseid + "不存在！\n");
				else {
					cvendorbaseid = ((String) map7.get(cvendorbaseid))
							.toString();
				}
				String cpurorganization = getString(cells[10].getContents());
				if ((getString((String) map12.get(cpurorganization)).equals(""))
						|| (getString((String) map12.get(cpurorganization)) == null))
					info.append(" 第"+(i+1)+"行【采购组织】" + cpurorganization + "不存在！\n");
				else {
					cpurorganization = ((String) map12.get(cpurorganization))
							.toString();
				}
				String vmemo = getString(cells[11].getContents());
				vo_head1.setCbiztype(cbiztype);
				vo_head1.setVordercode(vordercode);
				vo_head1.setCaccountbankid(caccountbankid);
				vo_head1.setCaccountyear(caccountyear);
				vo_head1.setCdeptid(cdeptid);
				vo_head1.setCemployeeid(cemployeeid);
				vo_head1.setCoperator(coperator);
				vo_head1.setCdeliveraddress(cdeliveraddress);
				vo_head1.setCvendormangid(cvendormangid);
				vo_head1.setCvendorbaseid(cvendorbaseid);
				vo_head1.setCpurorganization(cpurorganization);
				vo_head1.setVmemo(vmemo);
				vo_head1.setPk_corp(pk_corp);
				String str = sdf.format(date);
				vo_head1.setDorderdate(UFDate.getDate(str));
				vo_head1.setDauditdate(UFDate.getDate(str));
				vo_head1.setBdeliver(UFBoolean.FALSE);
				vo_head1.setBislatest(UFBoolean.TRUE);
				vo_head1.setBisreplenish(UFBoolean.FALSE);
				vo_head1.setBreturn(UFBoolean.FALSE);
				vo_head1.setForderstatus(Integer.valueOf(0));
				vo_head1.setNversion(Integer.valueOf(1));
				vo_head1.setStatus(2);
				vo_head1.setNtaxrate(new UFDouble(17));
				vo_head1.setNexchangeotobrate(new UFDouble(1));
				String cmangid = getString(cells[12].getContents());
				if ((getString(map15.get(cmangid)).equals(""))) {
					if (getString(map8.get(cmangid)).equals("")) {
						info.append(" 第"+(i+1)+"行【存货管理】" + cmangid + "不存在！\n");
					} else {
						cmangid = (map8.get(cmangid)).toString();
					}

				} else {
					String code = map15.get(cmangid).toString();
					if ((getString(map8.get(code)).equals(""))) {
						info.append("第"+(i+1)+"行 【存货管理】" + code + "不存在！\n");
					} else {
						cmangid = (map8.get(code)).toString();

					}
				}

				String cbaseid = getString(cells[13].getContents());
				if ((getString((String) map15.get(cmangid)).equals(""))) {
					if ((getString((String) map9.get(cbaseid)).equals(""))) {
						info.append(" 第"+(i+1)+"行【存货基本】" + cbaseid + "不存在！\n");
					} else {
						cbaseid = ((String) map9.get(cbaseid)).toString();
					}
				} else {
					String code = map15.get(cbaseid).toString();
					if ((getString((String) map9.get(code)).equals(""))) {
						info.append(" 第"+(i+1)+"行【存货基本】" + code + "不存在！\n");
					} else {
						cmangid = ((String) map9.get(code)).toString();

					}
				}

				String nordernum = getString(cells[14].getContents());
				String pk_arrvstoorg = getString(cells[15].getContents());
				if ((pk_arrvstoorg != null) && (!pk_arrvstoorg.equals(""))) {
					if ((getString((String) map13.get(pk_arrvstoorg))
							.equals(""))
							|| (getString((String) map13.get(pk_arrvstoorg)) == null))
						info.append(" 第"+(i+1)+"行【 库存组织】" + pk_arrvstoorg + "不存在！\n");
					else {
						pk_arrvstoorg = ((String) map13.get(pk_arrvstoorg))
								.toString();
					}
				}
				String norgtaxprice = getString(cells[16].getContents());
				String norgnettaxprice = getString(cells[17].getContents());
				String noriginalcurprice = getString(cells[18].getContents());
				String noriginalnetprice = getString(cells[19].getContents());
				String noriginaltaxpricemny = getString(cells[20].getContents());
				String noriginalcurmny = getString(cells[21].getContents());
				String noriginaltaxmny = getString(cells[22].getContents());
				String ntaxpricemny = getString(cells[23].getContents());
				String nmoney = getString(cells[24].getContents());
				String ntaxmny = getString(cells[25].getContents());
				String cwarehouseid = getString(cells[26].getContents());
				if ((cwarehouseid != null) && (!cwarehouseid.equals(""))) {
					if ((getString((String) map11.get(cwarehouseid)).equals(""))
							|| (getString((String) map11.get(cwarehouseid)) == null))
						info.append(" 第"+(i+1)+"行【 收货仓库】" + cwarehouseid + "不存在！\n");
					else {
						cwarehouseid = ((String) map11.get(cwarehouseid))
								.toString();
					}
				}
				String ccurrencytypeid = getString(cells[27].getContents());
				if ((getString((String) map10.get(ccurrencytypeid)).equals(""))
						|| (getString((String) map10.get(ccurrencytypeid)) == null))
					info.append(" 第"+(i+1)+"行【原币币种】" + ccurrencytypeid + "不存在！\n");
				else {
					ccurrencytypeid = ((String) map10.get(ccurrencytypeid))
							.toString();
				}
				vo_body1[0] = new OrderItemVO();
				vo_body1[0].setCrowno("10");
				vo_body1[0].setCmangid(cmangid);
				vo_body1[0].setCbaseid(cbaseid);
				vo_body1[0].setNordernum(new UFDouble(nordernum));
				vo_body1[0].setNorgtaxprice(new UFDouble(norgtaxprice));
				vo_body1[0].setNorgnettaxprice(new UFDouble(norgnettaxprice));
				vo_body1[0]
						.setNoriginalcurprice(new UFDouble(noriginalcurprice));
				vo_body1[0]
						.setNoriginalnetprice(new UFDouble(noriginalnetprice));
				vo_body1[0].setNoriginaltaxpricemny(new UFDouble(
						noriginaltaxpricemny));
				vo_body1[0].setNoriginalcurmny(new UFDouble(noriginalcurmny));
				vo_body1[0].setNoriginaltaxmny(new UFDouble(noriginaltaxmny));
				vo_body1[0].setNtaxpricemny(new UFDouble(ntaxpricemny));
				vo_body1[0].setNmoney(new UFDouble(nmoney));
				vo_body1[0].setNtaxmny(new UFDouble(ntaxmny));
				vo_body1[0].setCwarehouseid(cwarehouseid);
				vo_body1[0].setPk_arrvstoorg(pk_arrvstoorg);
				vo_body1[0].setCcurrencytypeid(ccurrencytypeid);
				vo_body1[0].setIdiscounttaxtype(Integer.valueOf(1));
				vo_body1[0].setIisactive(Integer.valueOf(0));
				vo_body1[0].setIisreplenish(Integer.valueOf(0));
				vo_body1[0].setNdiscountrate(new UFDouble(100));
				vo_body1[0].setNexchangeotobrate(new UFDouble(1));
				vo_body1[0].setNtaxrate(new UFDouble(17));
				vo_body1[0].setBreceiveplan(UFBoolean.FALSE);
				vo_body1[0].setForderrowstatus(Integer.valueOf(0));
				vo_body1[0].setPk_arrvcorp(pk_corp);
				vo_body1[0].setPk_reqstoorg(pk_corp);
				vo_body1[0].setPk_invoicecorp(pk_corp);
				vo_body1[0].setBlargess(UFBoolean.FALSE);
				vo_body1[0].setStatus(2);
				vo_body1[0].setPk_corp(pk_corp);
				vo_head1.setCcurrencytypeid(ccurrencytypeid);
//				vo_head1.setVdef20(info.toString());
				vo_hd.setParentVO(vo_head1);
				vo_hd.setChildrenVO(vo_body1);
				vo_hd.setStatus(2);
				list.add(vo_hd);
			}
		}

        if (!"".equals(info.toString())) {
			    throw new BusinessException(info.toString());
        }else{
    		if ((list != null) && (list.size() > 0))
    			vo_hb1 = (OrderVO[]) list.toArray(new OrderVO[0]);
        }
	}

	public static boolean checkStringToNum(String str) {
		Pattern pattern = Pattern.compile("[0.000000-9.000000]*");
		Matcher isNum = pattern.matcher(str);
		boolean isnum = isNum.matches();
		return isnum;
	}

	public String getString(String obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : obj.toString().trim();
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public String getString(Object obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : obj.toString().trim();
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public String getStringFromExe(Object obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : String.valueOf(obj).trim();
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}
}