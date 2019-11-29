package nc.ui.mo.hgz.zxgl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.trade.business.HYPubBO;
import nc.itf.mm.hgz.IPrintHgz;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wrback.state.IWriteBackStateService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.print.PrintEntry;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.HGZCardPanelPRTS;
import nc.ui.trade.pub.HGZListPanelPRTS;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.ic601.InvOnHandItemVO;
import nc.vo.mm.hgz.button.IBHgzZgButton;
import nc.vo.mo.hgz.HgzHeadVO;
import nc.vo.mo.hgz.onhand.OnhandVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFTime;
import nc.vo.uap.busibean.exception.BusiBeanException;

public class ZxglHandler extends ManageEventHandler{
	
	//add by zy 2019-08-26
	private ClientEnvironment ce = ClientEnvironment.getInstance();
	//end by zy

	public ZxglHandler(BillManageUI billUI, IControllerBase hgzController) {
		super(billUI, hgzController);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onBoQuery() throws Exception{
		
		super.onBoQuery();
	}
	
	@Override
	protected void onBoEdit() throws Exception{
		Object isxx=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bsxx").getValueObject();
		if(isxx!=null&&isxx.toString().equals("Y")){
			getBillUI().showWarningMessage("��ǰ�������������߼��鵥���޷��༭��");
		}else{
			super.onBoEdit();
		}
		
	}
	
	@Override
	protected void onBoDelete() throws Exception{
		Object isxx=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("bsxx").getValueObject();
		if(isxx!=null&&isxx.toString().equals("Y")){
			getBillUI().showWarningMessage("��ǰ�������������߼��鵥���޷�ɾ����");
		}else{
			super.onBoDelete();
		}
		
	}
	
	@Override
	protected void onBoSave() throws Exception{
		String produceorder = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("produceorder").getValueObject()==null?
				"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("produceorder").getValueObject().toString();
		if(produceorder.length()==0){
			getBillUI().showWarningMessage("���������ű��");
			return;
		}
		//add by zy 2019-09-29  ����Ʒ��Ա�ǿ���֤
		String qualitycontroller = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qualitycontroller").getValueObject()==null?
				"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qualitycontroller").getValueObject().toString();
		if(qualitycontroller.length()==0){
			getBillUI().showWarningMessage("Ʒ��Ա���");
			return;
		}
		
		String nnumber = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nnumber").getValueObject()==null?
				"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nnumber").getValueObject().toString();
		if(nnumber.length()==0){
			getBillUI().showWarningMessage("�������");
			return;
		}
		//end
		
		createSapCode();//�Զ��������뱣��
		super.onBoSave();
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception{
		
		super.onBoAdd(bo);
		
		String pk_corp=	ClientEnvironment.getInstance().getCorporation().getPk_corp();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
//		String tph = getTph();
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("palletnumber").setValue(tph);
		
		//����������ʷ��̨�����¼�¼
		String platformnum = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("mplatform").getValueObject()==null?
				"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("mplatform").getValueObject().toString();
		setDefaultInfo(platformnum,pk_corp);
		getScxScddh();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}
	
	/**
	 * ��ȡ���̺�
	 * @return
	 *//*
	public String getTph(){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    	String temp = "00"+sf.format(new Date()).substring(4,8);
		StringBuffer sql = new StringBuffer();
		sql.append(" select max(to_number(palletnumber))+1  as num from mm_hgz where palletnumber like '"+temp+"%' ");
		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object obj = null;
		try {
			obj = uap.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		String tph = "";
		if(obj==null){
			DecimalFormat g1=new DecimalFormat("00000000");
			tph = temp+"01";
			tph = g1.format(Integer.valueOf(tph.toString()));
		}else{
			DecimalFormat g1=new DecimalFormat("00000000");
	        tph = g1.format(Integer.valueOf(obj.toString()));
		}
		
		return tph;
	}*/
	
	@Override
	protected void onBoElse(int intBtn) throws Exception{
		switch (intBtn){
		case IBHgzZgButton.sctm:
			createSapCode();
				break;
		case IBHgzZgButton.scxxjyd:
			CreateLinCheck();
			break;				
		}
	}

	
	/**
	 * ��������
	 * 2019-8-7
	 * @author ���ű�
	 * @throws Exception 
	 */
	protected void createSapCode() throws Exception{
		/*
		 * ��ȡsap���ϱ���
		 * 
		 */
		String sapwlcode="60000037";
		
		/*
		 * ��ȡ��������
		 */
		String gccode="2031429"; 
		
		/*
		 * ��ȡ��������
		 */
		String dproducedate=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dproducedate").getValue();
		 if("".equals(dproducedate) || dproducedate==null){
			 throw new BusiBeanException("�������ڲ���Ϊ�գ�����������ԣ�");	
		 }
		String[] d= dproducedate.split("-");
		StringBuffer str=new StringBuffer();
		for(int j=0;j<d.length;j++){
			str.append(d[j]);
		}
		String pdate=str.substring(3);
		/*
		 * ��ȡ���
		 */
		String shifts=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("shifts").getValue();
		if(shifts==null||"".endsWith(shifts) ){ 
			throw new BusiBeanException("��β���Ϊ�գ�����������ԣ�");
		}
		if(getBc().get(shifts)==null){
			throw new BusiBeanException("���δ��ö�Ӧ��ţ�");
		}else{
			shifts = getBc().get(shifts).toString();
		}
		/*
		 * ��ȡ��̨��
		 */
		String mplatform=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("mplatform").getValue();
		if( mplatform==null||"".endsWith(mplatform) ){
			throw new BusiBeanException("��̨����Ϊ�գ�����������ԣ�");
		}
		
		/*
		 * ��ȡ���ʹ����
		 */
		String zjsyqok = getZJSYQ(dproducedate,18);
		String[] z= zjsyqok.split("-");
		StringBuffer stz=new StringBuffer();
		for(int j=0;j<z.length;j++){
			stz.append(z[j]);
		}
		String zdate=stz.substring(3);
		
		
		/*
		 * ��ȡ������
		 */
		String pch=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("batchnumbercode").getValue();
		if(pch==null||"".endsWith(pch) ){
			throw new BusiBeanException("���κŲ���Ϊ�գ�����������ԣ�");
		}
		IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
		String pk_corp=	ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
		String pk_hgz=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_hgz").getValue();
		int aq=writeback.findpch(pch,pk_corp,pk_hgz);
		DecimalFormat g1=new DecimalFormat("000");
        String zsdh = g1.format(Integer.valueOf(aq));
		
		
		/*
		 * ��������
		 */
		String b=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isprintcokecode").getValue()==null?"false":this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isprintcokecode").getValue().toString();
		StringBuffer sapcode=new StringBuffer();
		if(b.equals("true")){
			sapcode.append(sapwlcode);
			sapcode.append(gccode);
			sapcode.append(pdate);
			sapcode.append(shifts);
			sapcode.append(mplatform);
			sapcode.append(zdate);
			sapcode.append(zsdh);
		}else{
			sapcode.append("00000000");
			sapcode.append("0000000");
			sapcode.append(pdate);
			sapcode.append(shifts);
			sapcode.append(mplatform);
			sapcode.append(zdate);
			sapcode.append(zsdh);
		}
		
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sapcode").setValue(sapcode);
		
		
	}
	
	//���ʹ����
	public String getZJSYQ(String scrq,int zjrq) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dNow = sdf.parse(scrq);
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //�õ�����
		calendar.setTime(dNow);//�ѵ�ǰʱ�丳������
		calendar.add(calendar.MONTH, zjrq); //����Ϊ��9��
		dBefore = calendar.getTime(); //�õ���9�µ�ʱ��
		String defaultStartDate = sdf.format(dBefore); //��ʽ����9�µ�ʱ��
		
		UFDate sdate = new UFDate(defaultStartDate);
		UFDate tmp = sdate.getDateAfter(-1);//���ʹ������ǰ��һ��
		String backdate = tmp.toString();
		return backdate;
	}
	
	
	/**
	 * ��ӡ
	 * 
	 */
	 @Override
	protected void onBoPrint() throws Exception {
		String sapcode= this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sapcode").getValue();
		
		String img = "";
		if(sapcode!=null &&sapcode.length()>0){
			 img = CreateQRCode(sapcode);
		}
		
		if(((BillManageUI) getBillUI()).isListPanelSelected())
        {
			nc.ui.pub.print.IDataSource dataSource = new HGZListPanelPRTS(getBillUI()._getModuleCode(),((BillManageUI)getBillUI()).getBillListPanel());

			PrintEntry print = new PrintEntry(null, dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(), getBillUI()._getOperator(), getBillUI().getBusinessType(), getBillUI().getNodeKey());

			if (print.selectTemplate() == 1){
       		 	print.preview();
       	 	} 
        } else{
//        	  nc.ui.pub.print.IDataSource dataSource = new ListPanelPRTS(getBillUI()._getModuleCode(), ((BillManageUI)getBillUI()).getBillListPanel());
        	 nc.ui.pub.print.IExDataSource dataSource = new HGZCardPanelPRTS(getBillUI()._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),img);

        	 PrintEntry print = new PrintEntry(null, dataSource);
        	 print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(), getBillUI()._getOperator(), getBillUI().getBusinessType(), getBillUI().getNodeKey());

        	 if (print.selectTemplate() == 1){
        		 print.preview();
        	 }        	     
        }
		
	}
	
	
	/**
	 * �������߼��鵥
	 * 2019-8-7
	 */
	protected void CreateLinCheck(){
		IVOPersistence vo = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
		List<GlHeadVO> list=new ArrayList<GlHeadVO>();
		HgzHeadVO hgzVO=null;
		 int[] rows = ((ZxglUI) getBillUI()).getBillListPanel().getHeadTable().getSelectedRows();
		  if(rows==null || rows.length<=0){
			  NCOptionPane.showMessageDialog(getBillUI(), "û�м�¼,��ѡ��");
			    return;
		  }
		 
		 StringBuffer sucStr = new StringBuffer("");
		 StringBuffer faileStr = new StringBuffer("");
		 StringBuffer warnStr = new StringBuffer("");
		for(int j=0;j<rows.length;j++){
				GlHeadVO glHeandVO	=new GlHeadVO();
				GlItemBVO gleItemBVO=new GlItemBVO();
				String pk_glzb=null;
				 hgzVO=(HgzHeadVO) getBufferData().getVOByRowNo(rows[j]).getParentVO();
				 if(hgzVO.getVdef3()==null){
						warnStr.append("�к�"+(rows[j]+1)+"�м���������Ϊ�գ�");
					    continue;
					}
				 String bsxx=hgzVO.getBsxx();
				 String produceorder=hgzVO.getProduceorder();
				 String productname=hgzVO.getProductname();
				 String pk_hgz=hgzVO.getPk_hgz();
				 String pk_corp=hgzVO.getPk_corp();
				 String remarks=hgzVO.getRemarks();
				 String customername=hgzVO.getCustomername();
				 String ph=hgzVO.getBatchnumbercode();
				 if(!"Y".equals(bsxx)){
					HashMap momap = findMoInfo(produceorder,pk_corp);
					if(momap!=null){
						Integer ddwgsl = momap.get("ddwgsl")==null?new Integer(0):new Integer(momap.get("ddwgsl").toString());
						glHeandVO.setDdwgsl(ddwgsl);
						Integer ddsl = momap.get("ddsl")==null?new Integer(0):new Integer(momap.get("ddsl").toString());
						glHeandVO.setDdsl(ddsl);
						Integer ddwwgsl = ddsl-ddwgsl;
						glHeandVO.setDdwwgsl(ddwwgsl);
						Integer zdsl = momap.get("zdsl")==null?new Integer(0):new Integer(momap.get("zdsl").toString());
						glHeandVO.setZdsl(zdsl);
					}
					glHeandVO.setScddh(produceorder);
					HashMap lhmap = findLH(productname,pk_corp);
					if(lhmap!=null){
						String invmandoc = lhmap.get("invmandoc")==null?"":lhmap.get("invmandoc").toString();
						glHeandVO.setCp(invmandoc);
					}
					
					glHeandVO.setCk(hgzVO.getVdef2());//�ֿ�
					glHeandVO.setScx(findSCX(getMachine().get(hgzVO.getMplatform()).toString(),pk_corp));//��̨��Ӧ������
					glHeandVO.setBz(findBZ(hgzVO.getShifts(), pk_corp));//��ζ�Ӧ����
					glHeandVO.setPk_corp(pk_corp);
					glHeandVO.setBillsign("JYDJ");
					glHeandVO.setPhrq(hgzVO.getDproducedate());
					glHeandVO.setXxrq(hgzVO.getDproducedate());
					glHeandVO.setXxsj(new UFTime(hgzVO.getProductiontime()));
					glHeandVO.setKs(customername);
					try {
						String billno=new HYPubBO().getBillNo("JYDJ", pk_corp, null, null);
						glHeandVO.setBillno(billno);
					} catch (UifException e1) {
						e1.printStackTrace();
					}
					glHeandVO.setDjzt(0);
					
					glHeandVO.setJyjg(Integer.valueOf(hgzVO.getVdef3()));//������
					glHeandVO.setXxry(hgzVO.getQualitycontroller());//Ʒ��Ա->������Ա
					try {
					  pk_glzb=vo.insertVO(glHeandVO);
					} catch (BusinessException e) {
						e.printStackTrace();
					}
					gleItemBVO.setDh(remarks);
					gleItemBVO.setPk_corp(pk_corp);
					gleItemBVO.setPk_glzb(pk_glzb);
//					gleItemBVO.setNote(pk_hgz);
					if(lhmap!=null){
						String lh = lhmap.get("invcode")==null?"":lhmap.get("invcode").toString();
						String cp = lhmap.get("invname")==null?"":lhmap.get("invname").toString();
						String invmandoc = lhmap.get("invmandoc")==null?"":lhmap.get("invmandoc").toString();
						gleItemBVO.setCinventoryid(invmandoc);
						gleItemBVO.setLh(lh);
						gleItemBVO.setCp(cp);
					}
					
					gleItemBVO.setPh(ph);
					gleItemBVO.setXxaglsl(Integer.valueOf(hgzVO.getNnumber()));
					try {
						vo.insertVO(gleItemBVO);
						//���ºϸ�֤��ӡ���Ƹǣ���������߱�ʶΪY	
						hgzVO.setBsxx("Y");
						vo.updateVO(hgzVO);	
						sucStr.append(rows[j]+1)
						.append(",");
						IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
						writeback.updateXX(pk_hgz,pk_glzb);
					} catch (BusinessException e) {
						NCOptionPane.showMessageDialog(getBillUI(), "�к�"+(rows[j]+1)+"�������߼��鵥�쳣��"+e.getMessage());
					    continue;
					}
				}else{
					faileStr.append(rows[j]+1)
					.append(",");
				}
				 
		}
		
		if(sucStr.length()>0&&faileStr.length()>0){
			if(warnStr.length()>0){
				getBillUI().showErrorMessage("�к�"+faileStr+"���������߼��鵥\n"+"�к�"+sucStr+"�ɹ��������߼��鵥!\n"+warnStr.toString());
			}else{
				getBillUI().showErrorMessage("�к�"+faileStr+"���������߼��鵥\n"+"�к�"+sucStr+"�ɹ��������߼��鵥!");
			}
		}else if(sucStr.length()>0){
			if(warnStr.length()>0){
				getBillUI().showErrorMessage("�к�"+sucStr+"�ɹ��������߼��鵥!\n"+warnStr.toString());
			}else{
				getBillUI().showWarningMessage("�к�"+sucStr+"�ɹ��������߼��鵥!");
			}
			
		}else if(faileStr.length()>0){
			if(warnStr.length()>0){
				getBillUI().showErrorMessage("�к�"+faileStr+"���������߼��鵥!\n"+warnStr.toString());
			}else{
				getBillUI().showErrorMessage("�к�"+faileStr+"���������߼��鵥!");
			}
		}else if(warnStr.length()>0){
			getBillUI().showErrorMessage(warnStr.toString());
		}
		
	}
	
	/**
	 * ��ѯ����������Ϣ
	 * @param productname
	 * @return
	 */
	public HashMap findMoInfo(String pk_moid,String pk_corp){
		HashMap code=null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select jhwgsl as ddsl ,sjwgsl as ddwgsl,zdy2 as zdsl from mm_mo   ") 
		.append(" where pk_moid = '"+pk_moid+"' ") 
		.append(" and nvl(dr,0) = 0 ") 
		.append(" and pk_corp = '"+pk_corp+"' ") ;

		
		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			code=(HashMap) uap.executeQuery(sql.toString(), new MapProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * ��ѯ�����Ϣ
	 * @param productname
	 * @return
	 */
	public HashMap findLH(String productname,String corp){
		HashMap code=null;
		/*String sql="select bas.invcode as invcode ,bas.invname as invname from bd_produce man " +
				"inner join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc " +
				"where man.pk_produce ='"+productname+"'";
		*/
		StringBuffer sql = new StringBuffer();
		sql.append(" select man.pk_invmandoc as invmandoc,bas.invcode as invcode, bas.invname as invname from bd_produce pro ") 
		.append(" inner join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc = pro.pk_invmandoc  ") 
		.append(" inner join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where pk_produce = '"+productname+"' ") 
		.append(" and nvl(man.dr,0) = 0 ") 
		.append(" and nvl(pro.dr,0) = 0 ") 
		.append(" and nvl(bas.dr,0) = 0 ") 
		.append(" and pro.pk_corp = '"+corp+"' ") 
		.append(" and man.pk_corp = '"+corp+"' "); 


		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			code=(HashMap) uap.executeQuery(sql.toString(), new MapProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	
	
	
	public String  CreateQRCode(String sapcode) throws Exception {
		IPrintHgz ipr=(IPrintHgz)NCLocator.getInstance().lookup(IPrintHgz.class.getName());
		String path = ipr.createQRCode(sapcode);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vdef1",path);		
		return path;
	}
	
	/**
	 * ��ζ����Ӧֵ������������װ
	 * @return
	 */
	public HashMap getBc(){
		HashMap map = new HashMap();
		map.put("A/M", 11);
		map.put("A/N", 12);
		map.put("B/M", 21);
		map.put("B/N", 22);
		map.put("C/M", 31);
		map.put("C/N", 32);
		return map;
	}
		  
	/**
	 * ��̨�Ŷ����Ӧֵ������������װ
	 * @return
	 */
	public HashMap getMachine(){
		HashMap map = new HashMap();
		map.put("01", "S8-1");
		map.put("02", "S8-2");
		map.put("03", "T-2");
		map.put("04", "T-3");
		map.put("05", "T-4");
		return map;
	}
		
	/*private String md5(String s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		// ����md5����
		md.update(s.getBytes());
		// digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
		// BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
		return new BigInteger(1, md.digest()).toString(16);
	}*/

	/**
	 * ��̨��Ӧ�����ߣ���ѯ������
	 * @param productname
	 * @return
	 */
	public String findSCX(String jt,String corp){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_wkid ") 
		.append("   from pd_wk ") 
		.append("  where gzzxbm = '"+jt+"' ") 
		.append("    and nvl(dr, 0) = 0 ") 
		.append("    and pk_corp = '"+corp+"' ") ;

		Object scx = null;
		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			scx = uap.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return scx==null?"":scx.toString();
	}
	
	
	/**
	 * ��ζ�Ӧ���飬��ѯ����
	 * @param productname
	 * @return
	 */
	public String findBZ(String bc,String corp){
		
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_pgaid ") 
		.append("          from pd_pga ") 
		.append("         where bzbm = '"+bc+"' ") 
		.append("           and nvl(dr, 0) = 0 ") 
		.append("           and pk_corp = '"+corp+"' ") ;


		Object bz = null;
		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			bz = uap.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return bz==null?"":bz.toString();
	}
	
	/**
	 * ������ʷ���»�̨����Ϣ
	 * @return
	 */
	public void setDefaultInfo(String platformNum,String corp){
		HgzHeadVO hgz = queryHgz("",corp);
		if(hgz!=null){//Ϊ�����һ���ֶ�����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("mplatform").setValue(hgz.getMplatform());//��̨
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("inlibrarytype").setValue(hgz.getInlibrarytype());//�������
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("layernumber").setValue(hgz.getLayernumber());//����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("thickness").setValue(hgz.getThickness());//���
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nnumber").setValue(hgz.getNnumber());//����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("stripnumber").setValue(hgz.getStripnumber());//����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("shifts").setValue(hgz.getShifts());//���
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qualitycontroller").setValue(hgz.getQualitycontroller());//Ʒ��Ա
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("producttype").setValue(hgz.getProducttype());//��Ʒ����
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("remarks").setValue(hgz.getRemarks());//��ע
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("material").setValue(hgz.getMaterial());//����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("glue").setValue(hgz.getGlue());//����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("modules").setValue(hgz.getModules());//ģ��
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("batchnumbercode").setValue(hgz.getBatchnumbercode());//���κ�
//			String tph = getNewTph(hgz.getPalletnumber());
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("palletnumber").setValue(tph);//���̺�
		}
	}
	
	/**
	 * ��ȡ��ǰ��̨���»�̨��
	 * ��̨=������+��+��ˮ��
	 * @param oldtph
	 * @return
	 */
	public String getNewTph(String oldtph){
		String scx = oldtph.substring(0,1);
		String year = oldtph.substring(1,3);
		DecimalFormat g1=new DecimalFormat("000000");
		String lsh = g1.format(Integer.valueOf(oldtph.substring(3,9))+1);//��ǰ��̨����ˮ+1
		
		Calendar cale = null;  
        cale = Calendar.getInstance();  
        int yearnum = cale.get(Calendar.YEAR);
        
		String newyear = String.valueOf(yearnum).substring(2,4);
		String newtph = "";
		if(newyear.equals(year)){
			newtph = scx+year+lsh;
		}else{
			newtph = scx+newyear+"000001";
		}
		return newtph;		
	}
	
	/**
	 * ��ѯ��ʷ���ºϸ�֤��Ϣ
	 * @return
	 */
	public HgzHeadVO queryHgz(String platformNum,String corp){
		
		StringBuffer sql = new StringBuffer();
		if(platformNum.length()>0){
			sql.append(" select * ") 
			.append("   from (select * ") 
			.append("           from mm_hgz ") 
			.append("          where mplatform = '"+platformNum+"' ") 
			.append("            and nvl(dr, 0) = 0 ") 
			.append("            and pk_corp = '"+corp+"' ") 
			.append("            and vdef7 = '���߸���' ") 
			.append("          order by ts desc) ") 
			.append("  where rownum = 1 ") ;

		}else{
			sql.append(" select * ") 
			.append("   from (select * ") 
			.append("           from mm_hgz ") 
			.append("          where nvl(dr, 0) = 0 ") 
			.append("            and pk_corp = '"+corp+"' ") 
			.append("            and vdef7 = '���߸���' ") 
			.append("          order by ts desc) ") 
			.append("  where rownum = 1 ") ;
		}
		
		HgzHeadVO vo = new HgzHeadVO();
		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			vo = (HgzHeadVO) uap.executeQuery(sql.toString(), new BeanProcessor(HgzHeadVO.class));
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//add by zy 2019-08-26
	//add by gt 2019-08-12   ���߼��鵥���Ƿ񲢶������ǡ��󣬵����ִ�����ѯ�����������ִ�����ѯ�Ĵ���
	   public  void onQueryProducts(String zdslnum, String cpid, String pgy,ArrayList handvoList,String mplatform) throws Exception {
		
			String billno=null;
			
			ZxglUI dlg = new ZxglUI();
			//��ȡ��ǰ��¼��˾��Ϣ 
			String pk_corp=	ClientEnvironment.getInstance().getCorporation().getPk_corp();
			String scx = findSCX(getMachine().get(mplatform).toString(),pk_corp);
			OnhandVO[] result = dlg.onQuery2(zdslnum, cpid, pgy,handvoList,scx);
			/*if (result != null) {
//				 billno=String.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject());
				GlItemBVO[] vos = new GlItemBVO[result.length];
				OnhandVO item = null;
				//���ñ�������
				for (int i = 0; i < result.length; i++) {
					GlItemBVO vo = new GlItemBVO();
					item = (OnhandVO) result[i];
					vo.setLh(item.getInvcode()== null ? ""
							: item.getInvcode().toString());//�Ϻ�
					vo.setPh(item.getVbatchcode() == null ? "" :item.getVbatchcode() .toString());//���κ�
					vo.setCp(item.getInvname()  == null ? ""
							:item.getInvname() .toString());//��Ʒ
					vo.setHw(item.getCspaceid()==null?"":item.getCspaceid().toString());
			
					vo.setIsolationckid(item.getCwarehouseid() == null ? ""
							: item.getCwarehouseid().toString());//�ֿ�id
					vo.setIsolationcpid(item.getCinventoryid() == null ? ""
							: item.getCinventoryid().toString());//��ƷID
					vo.setDh(item.getVfree()==null?"":item.getVfree()); //���
					if(item.getNum()!=null) //��������
					{
						vo.setXxaglsl(Integer.valueOf(item.getNum()));
					}

					vos[i] = vo;
				}
				//���ñ�ͷ����
				GlHeadVO headvo = new GlHeadVO();
				headvo.setPk_corp(ce.getCorporation().getPk_corp());
				headvo.setBillsign("isolation");
				headvo.setXxrq(ce.getDate());
				headvo.setXxry(ce.getUser().getPrimaryKey());
			
				// headvo.setCk(result.getChildrenVO()[0].getAttributeValue("storname")==null?"":result.getChildrenVO()[0].getAttributeValue("storname").toString());
				GlBillVO aVo = new GlBillVO();
				aVo.setParentVO(headvo);
				aVo.setChildrenVO(vos);
				this.getBillCardPanelWrapper().setCardData(aVo);
//				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue("123");
				((BillManageUI) getBillUI()).getBillCardPanel().setEnabled(true);
				this.getButtonManager().getButton(0).setEnabled(true);
				this.getBillUI().updateButtons();
			}*/
		}
	   /**--gt end--*/
	   //end by zy
	   
	   /**
		 * ���ݻ�̨��=���������ƹ�������������
		 */
		public void getScxScddh(){
			
			String platformnum =  getBillCardPanelWrapper().getBillCardPanel().getHeadItem("mplatform").getValueObject()==null?
						"":  getBillCardPanelWrapper().getBillCardPanel().getHeadItem("mplatform").getValueObject().toString();
			String plat = getMachine().get(platformnum)==null?"":getMachine().get(platformnum).toString();
			if(plat.length()>0){
				StringBuffer sql = new StringBuffer();
				sql.append(" select pk_wkid ") 
				.append(" 		  from pd_wk ") 
				.append(" 		 where pk_corp = '1108' ") 
				.append(" 		   and gzzxbm = '"+plat+"' ") 
				.append(" 		   and nvl(dr, 0) = 0 ") ;
				
				IUAPQueryBS sessionManager = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					String scxid = sessionManager.executeQuery(sql.toString(), new ColumnProcessor())==null?"":(String)sessionManager.executeQuery(sql.toString(), new ColumnProcessor());
					if(scxid.length()>0){
						UIRefPane refpane = (UIRefPane)  getBillCardPanelWrapper().getBillCardPanel().getHeadItem("produceorder").getComponent();
						refpane.getRefModel().addWherePart(" and mm_mokz.gzzxid  = '"+scxid+"' ");
					}
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
}
