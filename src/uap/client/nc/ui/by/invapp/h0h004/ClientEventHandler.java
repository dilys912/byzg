package nc.ui.by.invapp.h0h004;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.by.invapp.button.IBYButton;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.vo.by.invapp.h0h004.FreezeVO;
import nc.vo.by.invapp.h0h004.IchandnumImptempVO;
import nc.vo.by.invapp.h0h004.IchandnumVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
 
public class ClientEventHandler extends DefaultBDBillCardEventHandle{
	
	public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
		super(arg0, arg1);
	}

	private int res;
	private File txtFile = null;
	private UITextField txtfFileUrl = null;
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn==IBYButton.BTN_KCDR) {//�ڳ���浼��
			KCDR();
		}else if (intBtn==IBYButton.BTN_KCJY) {//�ڳ����У��
			KCJY();
		}else if (intBtn==IBYButton.BTN_GLDR) {//�����浼��
			GLDR();
		}else if (intBtn==IBYButton.BTN_GLJY) {//������У��
			GLJY();
		}
	}
	
	/**
	 * �ڳ���浼��
	 * */
	private void KCDR() {
	    UIFileChooser fileChooser = new UIFileChooser();
	    fileChooser.setAcceptAllFileFilterUsed(true);
	    this.res = fileChooser.showOpenDialog(getBillUI());
	    if (this.res == 0) {
	      getTFLocalFile().setText(fileChooser.getSelectedFile().getAbsolutePath());
	      this.txtFile = fileChooser.getSelectedFile();
	      String filepath = this.txtFile.getAbsolutePath();
	      final WriteExcelbyQCKC exceldata = new WriteExcelbyQCKC();
	      WriteExcelbyQCKC.creatFile(filepath);
	      Runnable checkRun = new Runnable()
	      {
	        @SuppressWarnings({ "unchecked", "static-access" })
			public void run() {
	          BannerDialog dialog = new BannerDialog(getBillUI());
	          dialog.start();
	          try {
	            exceldata.readData(0);
	            GeneralBillVO[] vo = WriteExcelbyQCKC.vo_hb1;
	            IchandnumImptempVO[] bfvo = WriteExcelbyQCKC.vo_hb2;
	            HashMap<String ,GeneralBillVO> map2 = new HashMap<String ,GeneralBillVO>();
	            for (int i = 0; i < vo.length; i++) {
	            	GeneralBillVO voi = vo[i];
	            	GeneralBillHeaderVO hvoi = voi.getHeaderVO();
	                String kcck = hvoi.getPk_calbody()+hvoi.getCwarehouseid();//�����֯+�ֿ�
	                if (!map2.containsKey(kcck)) {
	                  map2.put(kcck, voi);
	                } else {
	                	GeneralBillItemVO[] bvo = (GeneralBillItemVO[]) voi.getChildrenVO();
	                	GeneralBillVO aggvo = map2.get(kcck);
	                    GeneralBillItemVO[] aggbvo = (GeneralBillItemVO[])aggvo.getChildrenVO();
	                    GeneralBillItemVO[] newvo = new GeneralBillItemVO[bvo.length + aggbvo.length];
	                    for (int j = 0; j < bvo.length; j++) {
		                    bvo[j].setCrowno(String.valueOf((j + 1) * 10));
		                    newvo[j] = bvo[j];
	                    }
	                    for (int j = 0; j < aggbvo.length; j++) {
	                        aggbvo[j].setCrowno(String.valueOf((j + bvo.length + 1) * 10));
	                        newvo[(bvo.length + j)] = aggbvo[j];
	                    }
	                    map2.get(kcck).setChildrenVO(newvo);
	                 }
	              }

	              Iterator it = map2.entrySet().iterator();
	              if ((it != null) && it.hasNext()) {
	            	  List<GeneralBillVO> list = new ArrayList<GeneralBillVO>();
	                  while (it.hasNext()) {
		                  Map.Entry entry = (Map.Entry)it.next();
		                  GeneralBillVO value = (GeneralBillVO)entry.getValue();
		                  list.add(value);
	                  }
	                  //�Ƚ������� ����
	  	    	      HYPubBO_Client bb = new HYPubBO_Client();
	          		  bb.insertAry(bfvo);
	          		  //�ٽ����ڳ����ݵ���
	                  IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
	                  //begin�ڳ����ı�����̫�࣬�����ڴ�������ֽ���������Ϊ500������
	                  List<AggregatedValueObject> newlist = new ArrayList<AggregatedValueObject>();
	                  if (list!=null&&list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							GeneralBillVO aggvoi = list.get(i);
							GeneralBillItemVO[] bvo = (GeneralBillItemVO[]) aggvoi.getChildrenVO();
							GeneralBillHeaderVO hvoi = (GeneralBillHeaderVO) aggvoi.getHeaderVO();
							String billNo = BillcodeRuleBO_Client.getBillCode("40", _getCorp().getPk_corp(),null, null);
							hvoi.setVbillcode(billNo);
							if (bvo!=null&&bvo.length>500) {//����500��
								int count = 1;
								List<GeneralBillItemVO> listbvos = new ArrayList<GeneralBillItemVO>();
								for (int j = 0; j < bvo.length; j++) {
									GeneralBillItemVO bvoj = (GeneralBillItemVO) bvo[j];
									bvoj.setCrowno(String.valueOf((j%500+1) * 10));//�к����±���
									listbvos.add(bvoj);
									if ((j==count*500-1)||(j==bvo.length-1)) {//��500�л򵽴����һ�����ݣ�����VO��װ
										GeneralBillVO voss = new GeneralBillVO();
										//��ͷ
										GeneralBillHeaderVO voHead = new GeneralBillHeaderVO();
										String billNo2 = BillcodeRuleBO_Client.getBillCode("40", _getCorp().getPk_corp(),null, null);
										voHead.setVbillcode(billNo2);
										voHead.setCgeneralhid(null);
									    voHead.setPrimaryKey(null);
									    voHead.setPk_calbody(hvoi.getPk_calbody());
									    voHead.setCwarehouseid(hvoi.getCwarehouseid());
									    voHead.setVbillcode(null);//���ݺ�
									    voHead.setStatus(2);//2-����
									    voHead.setDbilldate(hvoi.getDbilldate());//��������
									    voHead.setClogdatenow(hvoi.getClogdatenow());//��¼����
									    voHead.setFbillflag(2);
									    voHead.setCbilltypecode("40");//��������
									    voHead.setFreplenishflag(new UFBoolean(false));
									    voHead.setIscalculatedinvcost(new UFBoolean(true));
									    voHead.setIsdirectstore(new UFBoolean(false));
									    voHead.setIsforeignstor(new UFBoolean(false));
									    voHead.setIsgathersettle(new UFBoolean(false));
									    voHead.setPk_corp(hvoi.getPk_corp());//��˾
									    voHead.setCoperatorid(hvoi.getCoperatorid()); // ��ǰ����Ա
									    voHead.setCoperatoridnow(hvoi.getCoperatoridnow()); // ��ǰ����Ա
									    voHead.setVnote(hvoi.getVnote());
										voss.setParentVO(voHead);
										voss.setChildrenVO(listbvos.toArray(new GeneralBillItemVO[0]));
										voss.setSaveBadBarcode(false);// �Ƿ񱣴�����
										voss.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_ADD;
										voss.m_sActionCode = "SAVEBASE";
										newlist.add(voss);
										listbvos = new ArrayList<GeneralBillItemVO>();
										count++;
									}
								}
							}else{//500�����ڲ�����ִ���
								newlist.add(aggvoi);
							}
						}
					  }
	                  //end�ڳ����ı�����̫�࣬�����ڴ�������ֽ���������Ϊ500������
	    			  ipubdmo.N_XX_Action( "SAVE", "40", _getDate().toString(), newlist);
	                  getBillUI().showHintMessage("�ڳ���浼��ɹ���");
	              }
	          } catch (Exception e){
	        	  getBillUI().showWarningMessage(e.getMessage());
	          } finally {
	              dialog.end();
	          }
	        }
	      };
	      new Thread(checkRun).start();
	    }
	  }


	/**
	 * �����浼��
	 * @throws BusinessException 
	 * 
	 * */
	private void GLDR() throws BusinessException {
	    UIFileChooser fileChooser = new UIFileChooser();
	    fileChooser.setAcceptAllFileFilterUsed(true);
	    this.res = fileChooser.showOpenDialog(getBillUI());
	    if (this.res == 0) {
	      getTFLocalFile().setText(fileChooser.getSelectedFile().getAbsolutePath());
	      this.txtFile = fileChooser.getSelectedFile();
	      String filepath = this.txtFile.getAbsolutePath();
	      final WriteExcelbyGLKC exceldata = new WriteExcelbyGLKC();
	      WriteExcelbyGLKC.creatFile(filepath);
	      Runnable checkRun = new Runnable()
	      {
	        @SuppressWarnings({ "static-access" })
			public void run() {
	          BannerDialog dialog = new BannerDialog(getBillUI());
	          dialog.start();
	          try {
	            exceldata.readData(0);
	            GeneralBillVO[] vo = WriteExcelbyGLKC.vo_hb1;
	            IchandnumImptempVO[] bfvo = WriteExcelbyGLKC.vo_hb2;
	    	    FreezeVO[] free = new FreezeVO[vo.length];
	            for (int i = 0; i < vo.length; i++) {
	            	GeneralBillVO voi = vo[i];
	            	GeneralBillHeaderVO hvoi = voi.getHeaderVO();
                	GeneralBillItemVO[] bvosi = (GeneralBillItemVO[]) voi.getChildrenVO();
                	GeneralBillItemVO bvoi = bvosi[0];
                	FreezeVO freevoi = new FreezeVO();
                	free[i] = freevoi;
                	free[i].setPk_corp(hvoi.getPk_corp());//��˾
        			free[i].setCcalbodyid(hvoi.getPk_calbody());//�����֯
        			free[i].setCwarehouseid(hvoi.getCwarehouseid());//�ֿ�
        			free[i].setCinvbasid(bvoi.getCinvbasid());//�����������
        			free[i].setCinventoryid(bvoi.getCinventoryid());//���������
        			free[i].setCspaceid(bvoi.getCspaceid());// ��λ
        			free[i].setVbatchcode(bvoi.getVbatchcode());//���κ�
        			free[i].setVfree1(bvoi.getFreeItemVO().getVfree1());//������
        			free[i].setNfreezenum(bvoi.getNinnum());//��������
        			free[i].setCfreezerid(hvoi.getCoperatorid());//������
        			free[i].setDtfreezetime(bvoi.getDbizdate());//��������
        			free[i].setBfrzhandflag(new UFBoolean(true));//�Ƿ��ֹ�����
        			free[i].setDr(new Integer(0));//ɾ����־
        			free[i].setCcorrespondcode (hvoi.getVnote());//��ע
	            }
	    	    HYPubBO_Client bb = new HYPubBO_Client();
    	    	//����ǰУ��
    	    	checkVOS(free);
    	    	//�Ƚ������ݱ���
        		bb.insertAry(bfvo);
    	    	//�ٱ��������
        		bb.insertAry(free);
        		getBillUI().showWarningMessage("�����浼��ɹ���");
	          } catch (Exception e){
	        	  getBillUI().showWarningMessage(e.getMessage());
	          } finally {
	              dialog.end();
	          }
	        }
	      };
	      new Thread(checkRun).start();
	    }
	  }

	/**
	 * ����ǰУ��
	 * */
	private void checkVOS(FreezeVO[] free) {
		
	}

	/**
	 * �ڳ����У��
	 * @throws BusinessException 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	private void KCJY() throws BusinessException {
		//�������������
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
		//��ѯNC�Ŀ���ڳ����
		StringBuffer ncsb = new StringBuffer();
		ncsb.append(" select stor.storcode, ") 
		.append("        inv.invcode, ") 
		.append("        carg.cscode, ") 
		.append("        b.vbatchcode, ") 
		.append("        b.vfree1, ") 
		.append("        b.dbizdate, ") 
		.append("        cu.custcode, ") 
		.append("        sum(nvl(b.ninnum, 0)) ninnum, ") 
		.append("        sum(nvl(b.nmny, 0)) nmny ") 
		.append("   from ic_general_h h ") 
		.append("  inner join ic_general_b b on h.cgeneralhid = b.cgeneralhid ") 
		.append("   left join ic_general_bb1 bb1 on bb1.cgeneralbid = b.cgeneralbid ") 
		.append("   left join bd_stordoc stor on stor.pk_stordoc = h.cwarehouseid ") 
		.append("   left join bd_invbasdoc inv on inv.pk_invbasdoc = b.cinvbasid ") 
		.append("   left join bd_cargdoc carg on carg.pk_cargdoc = bb1.cspaceid ") 
		.append("   left join bd_cubasdoc cu on cu.pk_cubasdoc = b.pk_cubasdoc ") 
		.append("  where nvl(h.dr, 0) = 0 ") 
		.append("    and nvl(b.dr, 0) = 0 ") 
		.append("    and h.cbilltypecode = '40' ") 
		.append("    and h.pk_corp = '"+_getCorp().getPk_corp()+"' ") 
		.append("  group by stor.storcode, ") 
		.append("           inv.invcode, ") 
		.append("           carg.cscode, ") 
		.append("           b.vbatchcode, ") 
		.append("           b.vfree1, ") 
		.append("           b.dbizdate, ") 
		.append("           cu.custcode "); 
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<IchandnumVO> nclist = (List<IchandnumVO>) qurey.executeQuery(ncsb.toString(),new BeanListProcessor(IchandnumVO.class));
		HashMap<String, IchandnumVO> nchashmap = new HashMap<String, IchandnumVO>();
		if (nclist!=null&&nclist.size()>0) {
			for (int i = 0; i < nclist.size(); i++) {
				IchandnumVO voi = nclist.get(i);
				String key = voi.getBodycode()+voi.getStorcode()+voi.getInvcode()+voi.getCscode()
				             +voi.getVbatchcode()+voi.getVfree1()+voi.getDbizdate()+voi.getCustcode();
				if (!nchashmap.containsKey(key)) {
					nchashmap.put(key, voi);
				}
			}
		}
		//��ѯ��ϵͳ�Ŀ���ڳ����
		StringBuffer oldsb = new StringBuffer();
		oldsb.append(" select stor.storcode, ") 
		.append("        chdz.invcode, ") 
		.append("        hwdz.cscode, ") 
		.append("        lsb.vbatchcode, ") 
		.append("        lsb.vfree1, ") 
		.append("        lsb.dbizdate, ") 
		.append("        lsb.vdef1 custcode, ") 
		.append("        sum(nvl(lsb.ninnum,0)) oldninnum, ") 
		.append("        sum(nvl(lsb.nmny,0)) oldnmny ") 
		.append("   from ic_onhandnum_imptemp lsb ") 
		.append("   left join bd_stordoc stor on stor.storcode = lsb.storcode ") 
		.append("                            and stor.pk_corp = '"+_getCorp().getPk_corp()+"' ") 
		.append("                            and nvl(stor.dr, 0) = 0 ") 
		.append("   left join (select distinct cscode, oldcscode ") 
		.append("                from bd_cargcontrastdoc ") 
		.append("               where pk_corp = '"+_getCorp().getPk_corp()+"' ") 
		.append("                 and nvl(dr, 0) = 0) hwdz on hwdz.oldcscode = lsb.cscode ") 
		.append("   left join (select distinct invcode, oldinvcode ") 
		.append("                from bd_invcontrastdoc ") 
		.append("               where pk_corp = '"+_getCorp().getPk_corp()+"' ") 
		.append("                 and nvl(dr, 0) = 0) chdz on chdz.oldinvcode = lsb.invcode ") 
		.append("  where lsb.vnote like 'importdata%' ") 
		.append("    and lsb.pk_corp = '"+_getCorp().getPk_corp()+"' ") 
		.append(" group by stor.storcode, ") 
		.append("        chdz.invcode, ") 
		.append("        hwdz.cscode, ") 
		.append("        lsb.vbatchcode, ") 
		.append("        lsb.vfree1, ") 
		.append("        lsb.dbizdate, ") 
		.append("        lsb.vdef1 "); 
		List<IchandnumVO> oldlist = (List<IchandnumVO>) qurey.executeQuery(oldsb.toString(),new BeanListProcessor(IchandnumVO.class));
		HashMap<String, IchandnumVO> oldhashmap = new HashMap<String, IchandnumVO>();
		if (oldlist!=null&&oldlist.size()>0) {
			for (int i = 0; i < oldlist.size(); i++) {
				IchandnumVO voi = oldlist.get(i);
				String key = voi.getBodycode()+voi.getStorcode()+voi.getInvcode()+voi.getCscode()
				             +voi.getVbatchcode()+voi.getVfree1()+voi.getDbizdate()+voi.getCustcode();
				if (!oldhashmap.containsKey(key)) {
					oldhashmap.put(key, voi);
				}
			}
		}
		//NC����ϵͳ�����ݱȽ�
		List<IchandnumVO> agglist = new ArrayList<IchandnumVO>();
		Iterator<String> keys = oldhashmap.keySet().iterator();//����ϵͳ����Ϊ׼
		while(keys.hasNext()){
			String key = keys.next();
			IchandnumVO oldvo = oldhashmap.get(key);
			IchandnumVO ncvo = nchashmap.get(key);
			UFDouble oldninnum = new UFDouble(0);
			UFDouble oldnmny = new UFDouble(0);
			UFDouble ninnum = new UFDouble(0);
			UFDouble nmny = new UFDouble(0);
			if (oldvo!=null) {
				oldninnum = oldvo.getOldninnum()==null?new UFDouble(0):oldvo.getOldninnum();
				oldnmny = oldvo.getOldnmny()==null?new UFDouble(0):oldvo.getOldnmny();
			}
			if (ncvo!=null) {
				ninnum = ncvo.getNinnum()==null?new UFDouble(0):ncvo.getNinnum();
				nmny = ncvo.getNmny()==null?new UFDouble(0):ncvo.getNmny();
			}
			if ((oldninnum.doubleValue()!=ninnum.doubleValue())||(oldnmny.doubleValue()!=nmny.doubleValue())) {
				oldvo.setNinnum(ninnum);
				oldvo.setNmny(nmny);
				agglist.add(oldvo);
			}
		}
		//���쳣���ݲ��������ʾ
		System.out.println(oldhashmap.size());
		System.out.println(nchashmap.size());
		System.out.println(agglist.size());
		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBodyValueVO(agglist.toArray(new IchandnumVO[0]));
		getBillUI().showWarningMessage("�ڳ����У��ɹ���");
	}

	/**
	 * ������У��
	 * @throws BusinessException 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	private void GLJY() throws BusinessException {
		//�������������
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
		//��ѯNC�ĸ������ڳ����
		StringBuffer ncsb = new StringBuffer();
		ncsb.append(" select cal.bodycode, ") 
		.append("        cal.bodyname, ") 
		.append("        stor.storcode, ") 
		.append("        stor.storname, ") 
		.append("        bas.invcode, ") 
		.append("        bas.invname, ") 
		.append("        carg.cscode, ") 
		.append("        carg.csname, ") 
		.append("        fze.vbatchcode, ") 
		.append("        fze.vfree1, ") 
		.append("        fze.dtfreezetime dbizdate, ") 
		.append("        sum(fze.nfreezenum) ninnum ") 
		.append("   from ic_freeze fze ") 
		.append("   left join bd_calbody cal on nvl(cal.dr, 0) = 0 ") 
		.append("                           and cal.pk_calbody = fze.ccalbodyid ") 
		.append("   left join bd_stordoc stor on nvl(stor.dr, 0) = 0 ") 
		.append("                            and stor.pk_stordoc = fze.cwarehouseid ") 
		.append("   left join bd_invbasdoc bas on nvl(bas.dr, 0) = 0 ") 
		.append("                             and bas.pk_invbasdoc = fze.cinvbasid ") 
		.append("   left join bd_cargdoc carg on nvl(carg.dr, 0) = 0 ") 
		.append("                            and carg.pk_cargdoc = fze.cspaceid ") 
		.append("  where nvl(fze.dr, 0) = 0 ") 
		.append("    and fze.bfrzhandflag = 'Y' ") 
		.append("    and fze.pk_corp = '"+_getCorp().getPk_corp()+"' ") 
		.append("    and fze.ccorrespondcode like 'importfreedata%' ") 
		.append("  group by cal.bodycode, ") 
		.append("           cal.bodyname, ") 
		.append("           stor.storcode, ") 
		.append("           stor.storname, ") 
		.append("           bas.invcode, ") 
		.append("           bas.invname, ") 
		.append("           carg.cscode, ") 
		.append("           carg.csname, ") 
		.append("           fze.vbatchcode, ") 
		.append("           fze.vfree1, ") 
		.append("           fze.dtfreezetime "); 
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<IchandnumVO> nclist = (List<IchandnumVO>) qurey.executeQuery(ncsb.toString(),new BeanListProcessor(IchandnumVO.class));
		HashMap<String, IchandnumVO> nchashmap = new HashMap<String, IchandnumVO>();
		if (nclist!=null&&nclist.size()>0) {
			for (int i = 0; i < nclist.size(); i++) {
				IchandnumVO voi = nclist.get(i);
				String key = voi.getBodycode()+voi.getStorcode()+voi.getInvcode()+voi.getCscode()+voi.getVbatchcode()+voi.getVfree1()+voi.getDbizdate();
				if (!nchashmap.containsKey(key)) {
					nchashmap.put(key, voi);
				}
			}
		}
		//��ѯ��ϵͳ�ĸ������ڳ����
		
		//NC����ϵͳ�����ݱȽ�
		
		//���쳣���ݲ��������ʾ
		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBodyValueVO(nclist.toArray(new IchandnumVO[0]));
	}
	
	private UITextField getTFLocalFile()
	  {
	    if (this.txtfFileUrl == null) {
	      try
	      {
	        this.txtfFileUrl = new UITextField();
	        this.txtfFileUrl.setName("txtfFileUrl");
	        this.txtfFileUrl.setBounds(270, 160, 230, 26);
	        this.txtfFileUrl.setMaxLength(2000);
	        this.txtfFileUrl.setEditable(false);
	      }
	      catch (Exception e) {
	        e.printStackTrace(); 
	      }
	    }
	    return this.txtfFileUrl;
	  }
	
}
