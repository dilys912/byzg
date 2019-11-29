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
		List<IchandnumImptempVO> listbf = new ArrayList<IchandnumImptempVO>();//���ݱ���
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		UFDate ufdate = UFDate.getDate(str);
		StringBuffer info = new StringBuffer("");//������Ϣ
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String user = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		Sheet ws = w.getSheet(sheetNum);
		rows = ws.getRows();
		
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		//���
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
				String newkey = getString(listinv.get(j).get("invcode"));//�������
				String oldkey = getString(listinv.get(j).get("def4"));//���Ϻ�
				Object valueman = listinv.get(j).get("pk_invmandoc");//�������
				Object valuebas = listinv.get(j).get("pk_invbasdoc");//�������
				mapinvbas.put(newkey, valuebas);
				mapinvman.put(newkey, valueman);
				if (!"".equals(oldkey)) {//���ϺŲ�Ϊ��
					oldmapinvbas.put(oldkey, valuebas);
					oldmapinvman.put(oldkey, valueman);
					oldcodetonewcode.put(oldkey, newkey);
				}
			}
		}
		
		//�����֯
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
		
		//��λ
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
		
		//�ֿ�
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

		//�������
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

		//��λ����
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
				String bodycode = getString(cells[0].getContents());//�����֯����--1�����֯����
				String bodyname = getString(cells[1].getContents());//�����֯����--1�����֯����
				String storcode = getString(cells[2].getContents());//�ֿ����--3�ֿ�����
				String storname = getString(cells[3].getContents());//�ֿ����--3�ֿ�����
				String invbascode = getString(cells[4].getContents());//�������--5�������
				String invbasname = getString(cells[5].getContents());//�������--5�������
				String cscode = getString(cells[6].getContents());//��λ���--7��λ����
				String csname = getString(cells[7].getContents());//��λ���--7��λ����
				String vbatchcode = getString(cells[8].getContents());//���κ�
				String vfree1 = getString(cells[9].getContents());//������
				String rkrq = getString(cells[10].getContents());
				rkrq = rkrq.replaceAll("/", "-");
				UFDate dbizdate = getUFDate(rkrq);//�������
				UFDouble ninnum = getUFDouble(cells[11].getContents());//������ע�⵼��������ʾ�������뵥���Ƿ��ˡ�
				UFDouble nprice = getUFDouble(cells[12].getContents());//12����
				UFDouble nmny = getUFDouble(cells[13].getContents());//13���
				//��Ӧ��14
				String vnote = "importfreedata"+getString(cells[15].getContents());//��ע�����䣩
				
				//begin���ݱ����Ա��ڳ����У��
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
				//end���ݱ����Ա��ڳ����У��
				
				//��ͷ
				voHead.setCgeneralhid(null);
			    voHead.setPrimaryKey(null);
			    //�����֯
			    if (!"".equals(bodycode)) {
					String pk_calbody = getString(mapcal.get(bodycode));
					if (!"".equals(pk_calbody)) {
					    voHead.setPk_calbody(pk_calbody);
					}else{
						info.append("��"+(i+1)+"�п����֯����û����NCϵͳ���ҵ���Ӧ������\n");
						System.out.println("��"+(i+1)+"�п����֯����û����NCϵͳ���ҵ���Ӧ������");
					}
				}else{
					String pk_calbody = getString(mapcal.get("01"));
					if (!"".equals(pk_calbody)) {
					    voHead.setPk_calbody(pk_calbody);
					}else{
						info.append("��"+(i+1)+"�п����֯����(01)û����NCϵͳ���ҵ���Ӧ������\n");
						System.out.println("��"+(i+1)+"�п����֯����(01)û����NCϵͳ���ҵ���Ӧ������");
					}
				}
			    //�ֿ�
			    if (!"".equals(storcode)) {
					String cwarehouseid = getString(mapstor.get(storcode));
					if (!"".equals(cwarehouseid)) {
					    voHead.setCwarehouseid(cwarehouseid);
					}else{
						info.append("��"+(i+1)+"�вֿ����û����NCϵͳ���ҵ���Ӧ������\n");
						System.out.println("��"+(i+1)+"�вֿ����û����NCϵͳ���ҵ���Ӧ������");
					}
				}else{
					info.append("��"+(i+1)+"�вֿ����Ϊ�գ�\n");
					System.out.println("��"+(i+1)+"�вֿ����Ϊ�գ�");
				}
			    voHead.setVbillcode(null);//���ݺ�
			    voHead.setStatus(2);//2-����
			    voHead.setDbilldate(ufdate);//��������
			    voHead.setClogdatenow(ufdate.toString());//��¼����
			    voHead.setCbilltypecode("40");//��������
			    voHead.setPk_corp(pk_corp);//��˾
			    voHead.setCoperatorid(user); // ��ǰ����Ա
			    voHead.setVnote(vnote);
				//����
			    GeneralBillItemVO bvo = new GeneralBillItemVO();
			    voaItem[0] = bvo;
				voaItem[0].setCgeneralhid(null);
				voaItem[0].setCgeneralbid(null);
				voaItem[0].setPrimaryKey(null);
				voaItem[0].setStatus(2);//2-����
				//���
				if (!"".equals(invbascode)) {
					//1��������ձ��������´������
					String invcode = getString(mapinvcon.get(invbascode));
					if (!"".equals(invcode)) {
						String pk_invbasdoc = getString(mapinvbas.get(invcode));
						if (!"".equals(pk_invbasdoc)) {
							voaItem[0].setCinvbasid(pk_invbasdoc);//�����������
							voaItem[0].setCinvmanid(pk_invbasdoc);//�����������
							voaItem[0].setCinventorycode(invcode);//�������
							String pk_invmandoc = getString(mapinvman.get(invcode));
							if (!"".equals(pk_invmandoc)) {
								voaItem[0].setCinventoryid(pk_invmandoc);//�����������
							}else{
								info.append("��"+(i+1)+"�д������(����)û����NCϵͳ���ҵ���Ӧ�������������\n");
								System.out.println("��"+(i+1)+"�д������(����)û����NCϵͳ���ҵ���Ӧ�������������");
							}
						}else{
							info.append("��"+(i+1)+"�д������(����)û����NCϵͳ���ҵ���Ӧ�������������\n");
							System.out.println("��"+(i+1)+"�д������(����)û����NCϵͳ���ҵ���Ӧ�������������");
						}
					}else{//2��������ձ�û�����ݣ���Ϊ���ϺŲ��Ҵ������
						String pk_invbasdoc = getString(oldmapinvbas.get(invbascode));
						if (!"".equals(pk_invbasdoc)) {
							voaItem[0].setCinvbasid(pk_invbasdoc);//�����������
							voaItem[0].setCinvmanid(pk_invbasdoc);//�����������
							voaItem[0].setCinventorycode(oldcodetonewcode.get(invbascode));//�������
							String pk_invmandoc = getString(oldmapinvman.get(invbascode));
							if (!"".equals(pk_invmandoc)) {
								voaItem[0].setCinventoryid(pk_invmandoc);//�����������
							}else{
								info.append("��"+(i+1)+"�д������(���Ϻ�)û����NCϵͳ���ҵ���Ӧ�������������\n");
								System.out.println("��"+(i+1)+"�д������(���Ϻ�)û����NCϵͳ���ҵ���Ӧ�������������");
							}
						}else{
							//3�����Ϻ�Ҳû�����ݣ�ֱ����Ϊ�±����������
							String newpk_invbasdoc = getString(mapinvbas.get(invbascode));
							if (!"".equals(newpk_invbasdoc)) {
								voaItem[0].setCinvbasid(newpk_invbasdoc);//�����������
								voaItem[0].setCinvmanid(newpk_invbasdoc);//�����������
								voaItem[0].setCinventorycode(invbascode);//�������
								String newpk_invmandoc = getString(mapinvman.get(invbascode));
								if (!"".equals(newpk_invmandoc)) {
									voaItem[0].setCinventoryid(newpk_invmandoc);//�����������
								}else{
									info.append("��"+(i+1)+"�д������(��)û����NCϵͳ���ҵ���Ӧ�������������\n");
									System.out.println("��"+(i+1)+"�д������(��)û����NCϵͳ���ҵ���Ӧ�������������");
								}
							}else{
								info.append("��"+(i+1)+"�д������(��)û����NCϵͳ���ҵ���Ӧ�������������\n");
								System.out.println("��"+(i+1)+"�д������(��)û����NCϵͳ���ҵ���Ӧ�������������");
							}
						}
					}
				}else{
					info.append("��"+(i+1)+"�д������Ϊ�գ�\n");
					System.out.println("��"+(i+1)+"�д������Ϊ�գ�");
				}
				
				voaItem[0].setCrowno(String.valueOf(10));//�к�
				if (dbizdate!=null) {
					voaItem[0].setDbizdate(dbizdate);//�������
				}else{
					info.append("��"+(i+1)+"���������Ϊ�ջ��ʽ����ȷ��\n");
					System.out.println("��"+(i+1)+"���������Ϊ�ջ��ʽ����ȷ��");
				}
				voaItem[0].setNinnum(ninnum);//����
				if ("".equals(vbatchcode)) {
					voaItem[0].setVbatchcode(null);//���κ�Ϊ��
				}else{
					voaItem[0].setVbatchcode(vbatchcode);//���κŲ�Ϊ��
				}
			    //��λ
			    if (!"".equals(cscode)) {
					String newcscode = getString(mapCargcon.get(cscode));
					if (!"".equals(newcscode)) {
						String cspaceid = getString(mapcarg.get(newcscode));
						if (!"".equals(cspaceid)) {
							voaItem[0].setCspaceid(cspaceid);
							//��λVO
							LocatorVO voSpace = new LocatorVO();
							LocatorVO[] lvos = new LocatorVO[1];
							voSpace.setCspaceid(cspaceid);
							voSpace.setVspacename(getString(mapcargname.get(newcscode)));
							voSpace.setNinspacenum(ninnum);
							lvos[0] = voSpace;
							voaItem[0].setLocator(lvos);
							//��λVO
						}else{
							info.append("��"+(i+1)+"�л�λ����û����NCϵͳ���ҵ���Ӧ������\n");
							System.out.println("��"+(i+1)+"�л�λ����û����NCϵͳ���ҵ���Ӧ������");
						}
					}else{
						String cspaceid = getString(mapcarg.get(cscode));
						if (!"".equals(cspaceid)) {
							voaItem[0].setCspaceid(cspaceid);
							//��λVO
							LocatorVO voSpace = new LocatorVO();
							LocatorVO[] lvos = new LocatorVO[1];
							voSpace.setCspaceid(cspaceid);
							voSpace.setVspacename(getString(mapcargname.get(cscode)));
							voSpace.setNinspacenum(ninnum);
							lvos[0] = voSpace;
							voaItem[0].setLocator(lvos);
							//��λVO
						}else{
							info.append("��"+(i+1)+"�л�λ����(��)û����NCϵͳ���ҵ���Ӧ������\n");
							System.out.println("��"+(i+1)+"�л�λ����(��)û����NCϵͳ���ҵ���Ӧ������");
						}
					}
				}else{
					voaItem[0].setCspaceid(null);
				}
				FreeVO freevo = new FreeVO();
				if ("".equals(vfree1)) {
					freevo.setVfree1(null);//������Ϊ��
				}else{
					freevo.setVfree1(vfree1);//�����Ų�Ϊ��
				}
				voaItem[0].setFreeItemVO(freevo);
				//�ۺ�VO
			    voTempBill.setParentVO(voHead);
			    voTempBill.setChildrenVO(voaItem);
			    //����ͷ��������ĵ���VO�б�
				list.add(voTempBill);
			}
		}
		//�д�����Ϣ
		if (info!=null&&!"".equals(info.toString().trim())) {
			throw new BusinessException(info.toString());
		}
		
		if ((list != null) && (list.size() > 0)){
			vo_hb1 = (GeneralBillVO[]) list.toArray(new GeneralBillVO[0]);
			vo_hb2 = (IchandnumImptempVO[]) listbf.toArray(new IchandnumImptempVO[0]);//���ݱ���
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