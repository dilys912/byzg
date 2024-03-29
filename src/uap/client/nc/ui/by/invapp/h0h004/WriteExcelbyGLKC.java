package nc.ui.by.invapp.h0h004;

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
import nc.vo.by.invapp.h0h004.IchandnumImptempVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;

public class WriteExcelbyGLKC {
	public static Workbook w = null;
	public static int rows = 0;
	public static GeneralBillVO[] vo_hb1;
	public static IchandnumImptempVO[] vo_hb2;

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
	public void readData(int sheetNum) throws BusinessException {
		List<GeneralBillVO> list = new ArrayList<GeneralBillVO>();
		List<IchandnumImptempVO> listbf = new ArrayList<IchandnumImptempVO>();//数据备份
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		UFDate ufdate = UFDate.getDate(str);
		StringBuffer info = new StringBuffer("");//报错信息
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String user = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		Sheet ws = w.getSheet(sheetNum);
		rows = ws.getRows();
		
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		//存货
		String sqlinv = " select distinct invc.pk_invmandoc,inv.pk_invbasdoc, inv.invcode, inv.invname,invc.def4 " +
				" from bd_invmandoc invc " +
				" inner join bd_invbasdoc inv on inv.pk_invbasdoc = invc.pk_invbasdoc   and nvl(inv.dr, 0) = 0 " +
				" where invc.pk_corp ='" + pk_corp + "' and nvl(invc.dr, 0) = 0";
		ArrayList<HashMap<String, Object>> listinv = (ArrayList) iquery.executeQuery(sqlinv, new MapListProcessor());
		HashMap<String, Object> mapinvbas = new HashMap<String, Object>();
		HashMap<String, Object> mapinvman = new HashMap<String, Object>();
		HashMap<String, Object> oldmapinvbas = new HashMap<String, Object>();
		HashMap<String, Object> oldmapinvman = new HashMap<String, Object>();
		HashMap<String, String> oldcodetonewcode = new HashMap<String, String>();
		if ((listinv.size() > 0) && (listinv != null)) {
			for (int j = 0; j < listinv.size(); j++) {
				String newkey = getString(listinv.get(j).get("invcode"));//存货编码
				String oldkey = getString(listinv.get(j).get("def4"));//老料号
				Object valueman = listinv.get(j).get("pk_invmandoc");//存货管理
				Object valuebas = listinv.get(j).get("pk_invbasdoc");//存货基本
				mapinvbas.put(newkey, valuebas);
				mapinvman.put(newkey, valueman);
				if (!"".equals(oldkey)) {//老料号不为空
					oldmapinvbas.put(oldkey, valuebas);
					oldmapinvman.put(oldkey, valueman);
					oldcodetonewcode.put(oldkey, newkey);
				}
			}
		}
		
		//库存组织
		String sqlcal = "select distinct bodycode,bodyname,pk_calbody from bd_calbody where pk_corp = '"+pk_corp+"' and nvl(dr,0)=0";
		ArrayList<HashMap<String, Object>> listcal = (ArrayList) iquery.executeQuery(sqlcal, new MapListProcessor());
		HashMap<String, Object> mapcal = new HashMap<String, Object>();
		if ((listcal.size() > 0) && (listcal != null)) {
			for (int j = 0; j < listcal.size(); j++) {
				String key = getString(listcal.get(j).get("bodycode"));
				Object value = listcal.get(j).get("pk_calbody");
				mapcal.put(key, value);
			}
		}
		
		//货位
		StringBuffer sqlcarg = new StringBuffer();
		sqlcarg.append(" select distinct carg.cscode, carg.csname, carg.pk_cargdoc ") 
		.append("   from bd_cargdoc carg ") 
		.append("  inner join bd_stordoc stor on stor.pk_stordoc = carg.pk_stordoc ") 
		.append("  inner join bd_calbody cal on cal.pk_calbody = stor.pk_calbody ") 
		.append("  where cal.pk_corp = '"+pk_corp+"' ") 
		.append("    and nvl(carg.dr, 0) = 0 ") 
		.append("    and nvl(stor.dr, 0) = 0 ") 
		.append("    and nvl(cal.dr, 0) = 0 ") 
		.append("    and nvl(carg.sealflag, 'N') = 'N' ");

		ArrayList<HashMap<String, Object>> listcarg = (ArrayList) iquery.executeQuery(sqlcarg.toString(), new MapListProcessor());
		HashMap<String, Object> mapcarg = new HashMap<String, Object>();
		HashMap<String, Object> mapcargname = new HashMap<String, Object>();
		if ((listcarg.size() > 0) && (listcarg != null)) {
			for (int j = 0; j < listcarg.size(); j++) {
				String key = getString(listcarg.get(j).get("cscode"));
				Object csname = listcarg.get(j).get("csname");
				Object value = listcarg.get(j).get("pk_cargdoc");
				mapcarg.put(key, value);
				mapcargname.put(key, csname);
			}
		}
		
		//仓库
		String sqlstor = "select distinct pk_stordoc,storcode,storname from bd_stordoc where pk_corp='"
				+ pk_corp + "' and nvl(dr,0)=0";
		ArrayList<HashMap<String, Object>> liststor = (ArrayList) iquery.executeQuery(sqlstor, new MapListProcessor());
		HashMap<String, Object> mapstor = new HashMap<String, Object>();
		if ((liststor.size() > 0) && (liststor != null)) {
			for (int j = 0; j < liststor.size(); j++) {
				String key = getString(liststor.get(j).get("storcode"));
				Object value = liststor.get(j).get("pk_stordoc");
				mapstor.put(key, value);
			}
		}

		//存货对照
		String sqlinvcon = "select distinct invcode, oldinvcode from bd_invcontrastdoc  where  nvl(dr,0)=0 and  pk_corp='"
				+ pk_corp + "' ";
		ArrayList<HashMap<String, Object>> listinvcon = (ArrayList) iquery.executeQuery(sqlinvcon, new MapListProcessor());
		HashMap<String, Object> mapinvcon = new HashMap<String, Object>();
		if ((listinvcon.size() > 0) && (listinvcon != null)) {
			for (int j = 0; j < listinvcon.size(); j++) {
				String key = getString(listinvcon.get(j).get("oldinvcode"));
				Object value = listinvcon.get(j).get("invcode");
				mapinvcon.put(key, value);
			}
		}

		//货位对照
		String sqlCargcon = "select distinct cscode, oldcscode from bd_cargcontrastdoc  where  nvl(dr,0)=0 and  pk_corp='"
				+ pk_corp + "' ";
		ArrayList<HashMap<String, Object>> listCargcon = (ArrayList) iquery.executeQuery(sqlCargcon, new MapListProcessor());
		HashMap<String, Object> mapCargcon = new HashMap<String, Object>();
		if ((listCargcon != null)&&(listCargcon.size() > 0)) {
			for (int j = 0; j < listCargcon.size(); j++) {
				String key = getString(listCargcon.get(j).get("oldcscode"));
				Object value = listCargcon.get(j).get("cscode");
				mapCargcon.put(key, value);
			}
		}
		
		for (int i = 4; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			GeneralBillItemVO[] voaItem = new GeneralBillItemVO[1];
			GeneralBillHeaderVO voHead = new GeneralBillHeaderVO();
			GeneralBillVO voTempBill = new GeneralBillVO();
			if ((cells != null) && (cells.length != 0)) {
				String bodycode = getString(cells[0].getContents());//库存组织编码--1库存组织名称
				String bodyname = getString(cells[1].getContents());//库存组织编码--1库存组织名称
				String storcode = getString(cells[2].getContents());//仓库编码--3仓库名称
				String storname = getString(cells[3].getContents());//仓库编码--3仓库名称
				String invbascode = getString(cells[4].getContents());//存货编码--5存货名称
				String invbasname = getString(cells[5].getContents());//存货编码--5存货名称
				String cscode = getString(cells[6].getContents());//货位编号--7货位名称
				String csname = getString(cells[7].getContents());//货位编号--7货位名称
				String vbatchcode = getString(cells[8].getContents());//批次号
				String vfree1 = getString(cells[9].getContents());//单件号
				String rkrq = getString(cells[10].getContents());
				rkrq = rkrq.replaceAll("/", "-");
				UFDate dbizdate = getUFDate(rkrq);//入库日期
				UFDouble ninnum = getUFDouble(cells[11].getContents());//数量【注意导入后界面显示的数量与单价是否反了】
				UFDouble nprice = getUFDouble(cells[12].getContents());//12单价
				UFDouble nmny = getUFDouble(cells[13].getContents());//13金额
				//供应商14
				String vnote = "importfreedata"+getString(cells[15].getContents());//备注（必输）
				
				//begin数据备份以便期初库存校验
				IchandnumImptempVO impvo = new IchandnumImptempVO();
				impvo.setPk_corp(pk_corp);
				impvo.setBodycode(bodycode);
				impvo.setBodyname(bodyname);
				impvo.setStorcode(storcode);
				impvo.setStorname(storname);
				impvo.setInvcode(invbascode);
				impvo.setInvname(invbasname);
				impvo.setCscode(cscode);
				impvo.setCsname(csname);
				impvo.setVbatchcode(vbatchcode);
				impvo.setVfree1(vfree1);
				impvo.setNinnum(ninnum);
				impvo.setNprice(nprice);
				impvo.setNmny(nmny);
				impvo.setVnote(vnote);
				impvo.setDbizdate(dbizdate);
				listbf.add(impvo);
				//end数据备份以便期初库存校验
				
				//表头
				voHead.setCgeneralhid(null);
			    voHead.setPrimaryKey(null);
			    //库存组织
			    if (!"".equals(bodycode)) {
					String pk_calbody = getString(mapcal.get(bodycode));
					if (!"".equals(pk_calbody)) {
					    voHead.setPk_calbody(pk_calbody);
					}else{
						info.append("第"+(i+1)+"行库存组织编码没有在NC系统中找到对应主键！\n");
						System.out.println("第"+(i+1)+"行库存组织编码没有在NC系统中找到对应主键！");
					}
				}else{
					String pk_calbody = getString(mapcal.get("01"));
					if (!"".equals(pk_calbody)) {
					    voHead.setPk_calbody(pk_calbody);
					}else{
						info.append("第"+(i+1)+"行库存组织编码(01)没有在NC系统中找到对应主键！\n");
						System.out.println("第"+(i+1)+"行库存组织编码(01)没有在NC系统中找到对应主键！");
					}
				}
			    //仓库
			    if (!"".equals(storcode)) {
					String cwarehouseid = getString(mapstor.get(storcode));
					if (!"".equals(cwarehouseid)) {
					    voHead.setCwarehouseid(cwarehouseid);
					}else{
						info.append("第"+(i+1)+"行仓库编码没有在NC系统中找到对应主键！\n");
						System.out.println("第"+(i+1)+"行仓库编码没有在NC系统中找到对应主键！");
					}
				}else{
					info.append("第"+(i+1)+"行仓库编码为空！\n");
					System.out.println("第"+(i+1)+"行仓库编码为空！");
				}
			    voHead.setVbillcode(null);//单据号
			    voHead.setStatus(2);//2-新增
			    voHead.setDbilldate(ufdate);//单据日期
			    voHead.setClogdatenow(ufdate.toString());//登录日期
			    voHead.setCbilltypecode("40");//单据类型
			    voHead.setPk_corp(pk_corp);//公司
			    voHead.setCoperatorid(user); // 当前操作员
			    voHead.setVnote(vnote);
				//表体
			    GeneralBillItemVO bvo = new GeneralBillItemVO();
			    voaItem[0] = bvo;
				voaItem[0].setCgeneralhid(null);
				voaItem[0].setCgeneralbid(null);
				voaItem[0].setPrimaryKey(null);
				voaItem[0].setStatus(2);//2-新增
				//存货
				if (!"".equals(invbascode)) {
					//1、存货对照表查找最新存货编码
					String invcode = getString(mapinvcon.get(invbascode));
					if (!"".equals(invcode)) {
						String pk_invbasdoc = getString(mapinvbas.get(invcode));
						if (!"".equals(pk_invbasdoc)) {
							voaItem[0].setCinvbasid(pk_invbasdoc);//存货基本主键
							voaItem[0].setCinvmanid(pk_invbasdoc);//存货基本主键
							voaItem[0].setCinventorycode(invcode);//存货编码
							String pk_invmandoc = getString(mapinvman.get(invcode));
							if (!"".equals(pk_invmandoc)) {
								voaItem[0].setCinventoryid(pk_invmandoc);//存货管理主键
							}else{
								info.append("第"+(i+1)+"行存货编码(对照)没有在NC系统中找到对应存货管理主键！\n");
								System.out.println("第"+(i+1)+"行存货编码(对照)没有在NC系统中找到对应存货管理主键！");
							}
						}else{
							info.append("第"+(i+1)+"行存货编码(对照)没有在NC系统中找到对应存货基本主键！\n");
							System.out.println("第"+(i+1)+"行存货编码(对照)没有在NC系统中找到对应存货基本主键！");
						}
					}else{//2、存货对照表没有数据，作为老料号查找存货主键
						String pk_invbasdoc = getString(oldmapinvbas.get(invbascode));
						if (!"".equals(pk_invbasdoc)) {
							voaItem[0].setCinvbasid(pk_invbasdoc);//存货基本主键
							voaItem[0].setCinvmanid(pk_invbasdoc);//存货基本主键
							voaItem[0].setCinventorycode(oldcodetonewcode.get(invbascode));//存货编码
							String pk_invmandoc = getString(oldmapinvman.get(invbascode));
							if (!"".equals(pk_invmandoc)) {
								voaItem[0].setCinventoryid(pk_invmandoc);//存货管理主键
							}else{
								info.append("第"+(i+1)+"行存货编码(老料号)没有在NC系统中找到对应存货管理主键！\n");
								System.out.println("第"+(i+1)+"行存货编码(老料号)没有在NC系统中找到对应存货管理主键！");
							}
						}else{
							//3、老料号也没有数据，直接作为新编码查找主键
							String newpk_invbasdoc = getString(mapinvbas.get(invbascode));
							if (!"".equals(newpk_invbasdoc)) {
								voaItem[0].setCinvbasid(newpk_invbasdoc);//存货基本主键
								voaItem[0].setCinvmanid(newpk_invbasdoc);//存货基本主键
								voaItem[0].setCinventorycode(invbascode);//存货编码
								String newpk_invmandoc = getString(mapinvman.get(invbascode));
								if (!"".equals(newpk_invmandoc)) {
									voaItem[0].setCinventoryid(newpk_invmandoc);//存货管理主键
								}else{
									info.append("第"+(i+1)+"行存货编码(新)没有在NC系统中找到对应存货管理主键！\n");
									System.out.println("第"+(i+1)+"行存货编码(新)没有在NC系统中找到对应存货管理主键！");
								}
							}else{
								info.append("第"+(i+1)+"行存货编码(新)没有在NC系统中找到对应存货基本主键！\n");
								System.out.println("第"+(i+1)+"行存货编码(新)没有在NC系统中找到对应存货基本主键！");
							}
						}
					}
				}else{
					info.append("第"+(i+1)+"行存货编码为空！\n");
					System.out.println("第"+(i+1)+"行存货编码为空！");
				}
				
				voaItem[0].setCrowno(String.valueOf(10));//行号
				if (dbizdate!=null) {
					voaItem[0].setDbizdate(dbizdate);//入库日期
				}else{
					info.append("第"+(i+1)+"行入库日期为空或格式不正确！\n");
					System.out.println("第"+(i+1)+"行入库日期为空或格式不正确！");
				}
				voaItem[0].setNinnum(ninnum);//数量
				if ("".equals(vbatchcode)) {
					voaItem[0].setVbatchcode(null);//批次号为空
				}else{
					voaItem[0].setVbatchcode(vbatchcode);//批次号不为空
				}
			    //货位
			    if (!"".equals(cscode)) {
					String newcscode = getString(mapCargcon.get(cscode));
					if (!"".equals(newcscode)) {
						String cspaceid = getString(mapcarg.get(newcscode));
						if (!"".equals(cspaceid)) {
							voaItem[0].setCspaceid(cspaceid);
							//货位VO
							LocatorVO voSpace = new LocatorVO();
							LocatorVO[] lvos = new LocatorVO[1];
							voSpace.setCspaceid(cspaceid);
							voSpace.setVspacename(getString(mapcargname.get(newcscode)));
							voSpace.setNinspacenum(ninnum);
							lvos[0] = voSpace;
							voaItem[0].setLocator(lvos);
							//货位VO
						}else{
							info.append("第"+(i+1)+"行货位编码没有在NC系统中找到对应主键！\n");
							System.out.println("第"+(i+1)+"行货位编码没有在NC系统中找到对应主键！");
						}
					}else{
						String cspaceid = getString(mapcarg.get(cscode));
						if (!"".equals(cspaceid)) {
							voaItem[0].setCspaceid(cspaceid);
							//货位VO
							LocatorVO voSpace = new LocatorVO();
							LocatorVO[] lvos = new LocatorVO[1];
							voSpace.setCspaceid(cspaceid);
							voSpace.setVspacename(getString(mapcargname.get(cscode)));
							voSpace.setNinspacenum(ninnum);
							lvos[0] = voSpace;
							voaItem[0].setLocator(lvos);
							//货位VO
						}else{
							info.append("第"+(i+1)+"行货位编码(新)没有在NC系统中找到对应主键！\n");
							System.out.println("第"+(i+1)+"行货位编码(新)没有在NC系统中找到对应主键！");
						}
					}
				}else{
					voaItem[0].setCspaceid(null);
				}
				FreeVO freevo = new FreeVO();
				if ("".equals(vfree1)) {
					freevo.setVfree1(null);//单件号为空
				}else{
					freevo.setVfree1(vfree1);//单件号不为空
				}
				voaItem[0].setFreeItemVO(freevo);
				//聚合VO
			    voTempBill.setParentVO(voHead);
			    voTempBill.setChildrenVO(voaItem);
			    //单表头、单表体的单据VO列表
				list.add(voTempBill);
			}
		}
		//有错误信息
		if (info!=null&&!"".equals(info.toString().trim())) {
			throw new BusinessException(info.toString());
		}
		
		if ((list != null) && (list.size() > 0)){
			vo_hb1 = (GeneralBillVO[]) list.toArray(new GeneralBillVO[0]);
			vo_hb2 = (IchandnumImptempVO[]) listbf.toArray(new IchandnumImptempVO[0]);//数据备份
		}
	}

	public static boolean checkStringToNum(String str) {
		Pattern pattern = Pattern.compile("[0.000000-9.000000]*");
		Matcher isNum = pattern.matcher(str);
		boolean isnum = isNum.matches();
		return isnum;
	}

	  public static UFDouble getUFDouble(Object obj){
		  if(obj!=null){
			  try{
				  return new UFDouble(obj.toString().trim());
			  }catch(Exception e){
				  return new UFDouble(0);
			  }
		  }else{
			  return new UFDouble(0);
		  }
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

	  public static UFDate getUFDate(Object obj){
		  if(obj!=null){
			  try{
				  return new UFDate(obj.toString().trim());
			  }catch(Exception e){
				  return null;
			  }
		  }else{
			  return null;
		  }
	  }
}