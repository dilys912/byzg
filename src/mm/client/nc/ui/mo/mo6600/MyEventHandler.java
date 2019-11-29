package nc.ui.mo.mo6600;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.mm.scm.mm6600.IMo6600;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wrback.state.IWriteBackStateService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.sm.login.ShowDialog;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.query.INormalQuery;
import nc.uif.pub.exception.UifException;
import nc.utils.modify.is.IdetermineService;
import nc.vo.bd.CorpVO;
import nc.vo.mm.bzfs.BzfsBVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.config.Account;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends ManageEventHandler
{

	public MyEventHandler(BillManageUI billUI, IControllerBase control)
	{
		super(billUI, control);
	}
	@Override
	protected void onBoQuery() throws Exception
	{
		Account a = new Account();
		String b = a.getAccountCode();
		StringBuffer qryWhere = new StringBuffer();
	    StringBuffer strWhere = new StringBuffer();
		strWhere.append("pk_glzb in( select mm_glzb.pk_glzb from mm_glzb ");//add LGY
		if (askForQueryCondition(qryWhere) == false)
			
			return;// �û������˲�ѯ
		//add LGY
	if(qryWhere.indexOf("mm_glzb_b")>0)
	{
		strWhere.append(" inner join mm_glzb_b on mm_glzb_b.pk_glzb=mm_glzb.pk_glzb ");
	}
	else 
	{	
		strWhere.append(" where 1=1");
	}
	//add LGY
		strWhere.append("  and billsign='JYDJ' and  ").append(qryWhere.toString()+")");//add LGY
		
		SuperVO[] queryVos =HYPubBO_Client.queryByCondition(GlHeadVO.class, strWhere.toString());//queryHeadVOs(strWhere.toString());
     
		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
//		//add by src 2018��4��11��19:04:21
//		for(int i = 0; i<queryVos.length ;i++){
//			String scddh = queryVos[i].getAttributeValue("scddh") == null?"":queryVos[i].getAttributeValue("scddh").toString();
//			getBzfs(scddh);
//		}
        }

	private String getUnitPK()
	{
		return ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}

	protected void buttonActionAfter(AbstractBillUI abstractbillui, int intBtn)
			throws Exception
	{
		switch (intBtn)
		{
		case IBillButton.Add:
			initialAdd();
			break;
		case IBillButton.AddLine:
			initialAddLine();
		case IBillButton.InsLine:
			initialAddLine();
		default:
		} 
	}

	protected void initialAddLine()
	{
		try
		{
			UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable();
			BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel();
			int curRow = table.getSelectedRow();
			
			//��������/��������Ĭ�ϵ��ڱ�ͷ��������
			model.setValueAt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValue(), curRow, "xxaglsl");
			model.setValueAt(getUnitPK(), curRow, "pk_corp");
			// MoHeaderVO heads[] =
			// MMProxy.getRemoteMO().queryMoByWhere(" and mm_mo.pk_moid='"+((UIRefPane)getBillItem("scddh").getComponent()).getRefPK()+"'");
			UIRefPane pane = (UIRefPane) getHeadBillItem("cp").getComponent();
			model.setValueAt(pane.getRefName(), curRow, "cp");
			model.setValueAt(pane.getRefValue("bd_invbasdoc.invcode"), curRow,
					"lh"); 
			String dhName = this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("dh").getName();
			int colIndex = table.getColumnModel().getColumnIndex(dhName);
			table.editCellAt(curRow, colIndex);
			 
			this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(curRow, "lh");
			// model.setValueAt(pane.getRefName(), curRow, "lhmc");
			// }
			// this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("lh");
		} catch (Exception ex)
		{
			nc.bs.logging.Logger.error(ex);
			ex.printStackTrace();
		}

	} 

	protected void onBoEdit() throws Exception
	{
		if (((BillManageUI) getBillUI()).isListPanelSelected())
		{
			((BillManageUI) getBillUI()).setCurrentPanel("CARDPANEL");
			getBufferData().updateView();
		}
		if (getBufferData().getCurrentVO() == null)
			return;
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		getBillUI().setBillOperate(0);
		//add by src 2018��4��11��19:04:21
//		GlBillVO aggvo = (GlBillVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(GlBillVO.class.getName(), GlHeadVO.class.getName(), GlItemBVO.class.getName());
//		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
//        if(hvo.getPk_corp().equals("1016")){
//        	String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//        	getBzfs(scddh);
//		}
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		//by src 2018��4��11��19:05:43
//		GlBillVO aggvo = (GlBillVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(GlBillVO.class.getName(), GlHeadVO.class.getName(), GlItemBVO.class.getName());
//		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
//		if(hvo.getPk_corp().equals("1016")){
//			String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//			getBzfs(scddh);
//		}
	}
	

	/**
	 * ȷ��;
	 */
	protected void onConfirm() throws Exception
	{
		// �޸ĵ���״̬Ϊ�Ѽ���
		// getHeadBillItem("djzt").setValue(new Integer(1));
		GlBillVO billVO=null;
		if(this.getBillUI().getBillOperate()==2)
		{
			billVO = (GlBillVO) this.getBufferData().getCurrentVO();
		}else
		{
		  //wkf--����onBoSave()����Ϊ�ط��Ƿ񱣴�ɹ�--2015-01-09--start-
		  boolean stants = onBoSave2();
		  if(stants){
			  billVO = (GlBillVO) this.getBufferData().getCurrentVO();
		  }else{
			  return;
		  }
		  //--end--
		}
		//onBoRefresh();
		if (billVO != null)
		{
			if (!checkCofirmVO(billVO))
			{
				return;
			}
			// //������ⵥ
			((GlHeadVO)billVO.getParentVO()).setLogindate(ClientEnvironment.getInstance().getDate());
			IMo6600 im = (IMo6600) NCLocator.getInstance().lookup(
					IMo6600.class.getName());
			
			try
			{ 
				im.confirm(billVO);
				// ���ý���״̬
				setSaveOperateState();
				this.onBoRefresh();
				// �ص��б����
				// onBoReturn();
				//by src 2018��4��11��15:14:18
//				GlHeadVO hvo = (GlHeadVO) billVO.getParentVO();
//				if(hvo.getPk_corp().equals("1016")){
//					saveBzfs(billVO);
//					String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//					getBzfs(scddh);
//				}

				/**
				 * ���¶�Ӧ�ϸ�֤������Ӧ������ʶ
				 * @author ���ű�
				 * 2019-08-12
				 */
				//��ȡ��Ӧ�ı�ע�ֶε�ֵ�����ϸ�֤��ӡ������
				GlHeadVO hvo = (GlHeadVO) billVO.getParentVO();
				if(getUnitPK().equals("1078")||getUnitPK().equals("1108")){
					if(hvo!=null){
						String pk_glzb=hvo.getPk_glzb();
						//�޸ĺϸ�֤��ӡ���Ƹǣ�������ʶ
						IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
						writeback.writeBackRK(pk_glzb);
					}
				}
				
				
			} catch (Exception ex)
			{
				
				MessageDialog.showErrorDlg(getBillUI(),"����Error",ex.getMessage());
				Logger.error(ex);
			}
		}

	}	
	
	//�������κ�
	protected String generatePh(GlHeadVO head)
	{
		StringBuffer ph = new StringBuffer("");
		CorpVO curCorp = ClientEnvironment.getInstance().getCorporation();
		if(curCorp.getDef6()==null||curCorp.getDef6().equals(""))
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "�޷��������κţ���ǰ��¼��˾�ĵ����Ŵ��벻��Ϊ�գ�Unable to generate batch number, batch number code of the currently logged on the company can not be empty!");
			return null;
		}
		
		ph.append(curCorp.getDef6());
		
		//UFDate date = ClientEnvironment.getServerTime().getDate();		
		UFDate date = head.getPhrq();
		
		ph.append(date.toString().substring(2, 4));
		byte[] t = new byte[]{64};
		t[0] += date.getMonth();
		ph.append(new String(t));
		ph.append(date.toString().substring(8, 10));
		
		
		UIRefPane pane = (UIRefPane)this.getHeadItem("bz").getComponent();
		String bzmc = (String)pane.getRefValue("bzmc");
		if(bzmc==null||bzmc.trim().equals("")){
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "�޷��������κţ����鲻��Ϊ�գ�Can not generate the batch number, team can not be empty!");
			return null;
		}else if(bzmc.indexOf('(')<0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "�޷��������κţ��������������������κŵĹ���Ӧ����'(X',X��ʾ��ĸ��Unable to generate a batch number, team name does not comply with the rules that generate batch number should contain '(X', X represents the letter!");
			return null;
		}
		//ph.append(bzmc.substring(bzmc.indexOf("("),2));
		int bzIndex = bzmc.indexOf("(");
		ph.append(bzmc.substring(bzIndex+1, bzIndex + 2));
		
		pane = (UIRefPane)this.getHeadItem("scx").getComponent();
		String scxmc = (String)pane.getRefValue("gzzxmc");
		if(scxmc==null||scxmc.trim().equals(""))
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "�����߲���Ϊ�գ�The production line can not be empty!");
			return null;
		}
		ph.append(scxmc.contains(Transformations.getLstrFromMuiStr("��","Steel")) ? "1":scxmc.contains(Transformations.getLstrFromMuiStr("��2","aluminum")) ? "3":"2");	
		
//		UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
//		.getBillTable();
//		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
//		.getBillModel(); 
//		for(int i=0;i<table.getRowCount();i++)
//		{ 
//				model.setValueAt(ph.toString(), i, "ph"); 
//		}  
		
		return ph.toString();
	}

	protected GlBillVO save()
	{
		GlBillVO billVO = null;
		try
		{
			//add by yhj 2014-03-27
//			billVO = (GlBillVO) billvoChangedOld;
//			getBufferData().clear();
//			getBufferData().addVOToBuffer(billVO);
//			getBufferData().updateView();
			//end
			billVO = (GlBillVO) this.getBillCardPanelWrapper()
					.getBillVOFromUI();
			billVO.setParentVO(null);
			billVO.setChildrenVO(null);
			getBufferData().clear();
			billVO.setParentVO(billvoChangedOld.getParentVO()); 
			billVO.setChildrenVO(billvoChangedOld.getChildrenVO()); 
			
			getBufferData().addVOToBuffer(billVO);
			getBufferData().updateView();
			GlHeadVO head = (GlHeadVO) billVO.getParentVO();		
			GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();					
			
			if (vos == null)
			{
				return null;
			}
			
//			�������κ�
			//add by zwx 2019-8-16 �Ƹ�����ͨ���ϸ�֤����
			/*String ph = generatePh(head);
			if(ph == null)
			{
				return null;
			}*/
			String ph = null;
			if(!(head.getPk_corp().equals("1078")||head.getPk_corp().equals("1108"))){
				ph = generatePh(head);
			}else{
				ph = vos[0].getPh();
			}
			if(ph == null)
			{
				return null;
			}		
						
			if (head.getPk_corp() == null
					|| head.getPk_corp().trim().equals("")) // ���õ�ǰ��˾
			{
				head.setPk_corp(getUnitPK());
			}
			if (head.getLrr() == null || head.getLrr().trim().equals("")) // �����Ƶ���
			{
				head.setLrr(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
			}
			if (head.getBillsign() == null
					|| head.getBillsign().trim().equals(""))
			{
				head.setBillsign("JYDJ");
			}
			if (!checkVO(billVO))
			{
				return null;
			}
			int currXxsl=0;
			for (GlItemBVO temp : vos) 
			{
//				 �ж��Ƿ���ͷ��
				
				if (temp.getXxaglsl() != head.getDdsl())
				{
					temp.setSfltd(new UFBoolean(true));
				} else
				{
					temp.setSfltd(new UFBoolean(false));
				}
				if(temp.getStatus()==VOStatus.UNCHANGED)
				{
					temp.setStatus(VOStatus.UPDATED);
				}
				temp.setPh(ph);
				currXxsl+=temp.getXxaglsl();
			}
            if(!CheckNumberOfShipments(head.getScddh(),currXxsl))
            {
            	return null;
            }
			// ���浥��
			IMo6600 im = (IMo6600) NCLocator.getInstance().lookup(
					IMo6600.class.getName());
			for(int i=0;i<billVO.getChildrenVO().length;i++)
			{
				if((billVO.getChildrenVO()[i]).getStatus()==VOStatus.NEW)
				{
					(billVO.getChildrenVO()[i]).setAttributeValue("pk_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
				}
			}
			billVO = im.saveBill(billVO);
			// billVO = (GlBillVO) HYPubBO_Client.saveBill(billVO); // ���浥��

			// ��VO��ӵ�����
			getBufferData().clear();//���߼��鵥���������������ͬ�ĵ���--wkf---2014-11-21
			addVoToBuffer(billVO);
			this.updateBuffer();
			this.getBufferData().setCurrentVO(billVO);
			return billVO;
		} catch (Exception e)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "����Error", "����ʧ��Failed to save��"
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	protected void onBoDelete()
	{
		AggregatedValueObject billVO = this.getBufferData().getCurrentVO();
		GlHeadVO glHeadVO=(GlHeadVO)getBufferData().getCurrentVO().getParentVO();
		try
		{
			HYPubBO_Client.deleteBill(billVO);
			this.getBufferData().removeCurrentRow();

			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
			getBufferData().removeCurrentRow();
			if (getBufferData().getVOBufferSize() == 0)
			{
				getBillUI().setBillOperate(4);
			}else
			{
				getBillUI().setBillOperate(2); //IBillOperate.OP_NOTEDIT
			}
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			/**
			 * ���߼��鵥ɾ������ʱ����д�Ƿ�����״̬
			 */
			if(getUnitPK().equals("1078")||getUnitPK().equals("1108")){
				if(glHeadVO!=null){
					IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
					writeback.delWriteBackXX(glHeadVO.getPk_glzb());
				}
			}
			
			
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			Logger.error(e);
			e.printStackTrace();
		}

	}

	protected void addVoToBuffer(GlBillVO billVO) throws Exception
	{
		CircularlyAccessibleValueObject[] cirs = this.getBufferData()
				.getAllHeadVOsFromBuffer();
		if (cirs == null || cirs.length == 0)
		{
			this.getBufferData().addVOToBuffer(billVO);
			return;
		}
		for (int i = 0; i < cirs.length; i++)
		{
			if (cirs[i].getPrimaryKey().equals(
					billVO.getParentVO().getPrimaryKey()))
			{
				return;
			}
		}
		this.getBufferData().addVOToBuffer(billVO);
	}
	
	/**
	 * �����ŷǿ���֤
	 * @author yhj 2014-03-11
	 * @throws Exception
	 */
	public boolean checkNULL(AggregatedValueObject aggvo) throws Exception{
		if(aggvo != null){
			GlItemBVO[] gbvos = (GlItemBVO[]) aggvo.getChildrenVO();
			if(gbvos != null && gbvos.length > 0){
				for (int i = 0; i < gbvos.length; i++) {
					GlItemBVO bvo = gbvos[i];
					String dh = bvo.getDh();
					if(dh == null){
						Integer rowno = i + 1;
						MessageDialog.showErrorDlg(getBillUI(),"����", "�����"+rowno+"�У���Ų���Ϊ��,��������");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * ������ݸ�ʽ��֤��ֻ��Ϊ����
	 * @author yhj 2014-03-11
	 * @throws Exception
	 */
	public boolean checkDH(AggregatedValueObject aggvo) throws Exception{
		if(aggvo != null){
			GlItemBVO[] gbvos =  (GlItemBVO[]) aggvo.getChildrenVO(); 
			if(gbvos != null && gbvos.length > 0){
				List errorRow = new ArrayList();
				for (int i = 0; i < gbvos.length; i++) {
					GlItemBVO bvo = gbvos[i];
					String dh = bvo.getDh();
					java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[0-9]+(.[0-9]+)?");
					java.util.regex.Matcher match=pattern.matcher(dh);
					if(match.matches()==false){
						Integer rowno = i + 1;
						errorRow.add(rowno);
//						MessageDialog.showErrorDlg(getBillUI(), "����", "�����ŵ�"+rowno+"�г��ַǷ��ַ������ֻ��Ϊ����,��������");
//						return false;
					}else{
						continue;
					}
				}
				if(errorRow != null && errorRow.size() > 0){
					StringBuilder str = new StringBuilder();
					for (int i = 0; i < errorRow.size(); i++) {
						if(i == 0){
							str.append(i);
						}else if(i == errorRow.size() - 1){
							str.append(",");
							str.append(i);
						}else{
							str.append(",");
							str.append(i);
							str.append(",");
						}
					}
					MessageDialog.showErrorDlg(getBillUI(), "����", "�����ŵ�"+errorRow.toString()+"�г��ַǷ��ַ������ֻ��Ϊ����,��������");
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * �������
	 * @author yhj 2014-03-11
	 * @throws Exception
	 */
	public boolean onBoSortNo(AggregatedValueObject aggvo) throws Exception{
		if(aggvo != null){
			GlItemBVO[] gbvos = (GlItemBVO[]) aggvo.getChildrenVO();
			if(gbvos != null){
				if(!checkNULL(aggvo)) return false;//�ǿ�У��
				if(!(_getCorp().getPk_corp().equals("1078")||_getCorp().getPk_corp().equals("1108"))){//add by zwx 2019-8-15 �Ƹǲ�У��
					if(!checkDH(aggvo)) return false;//���ֻ��Ϊ���֣�У��
				}
				
				GlItemBVO tempvo = null;
				if(gbvos != null && gbvos.length > 0){
					//ȡ�ý����ϵ�VO��������棬VO������������ӵ�������
					for (int i = 0; i < gbvos.length; i++) {
						for (int j = i + 1; j < gbvos.length; j++) {
							if(BigInteger.valueOf(Long.valueOf(gbvos[i].getDh().trim().toString())).compareTo(
									BigInteger.valueOf(Long.valueOf(gbvos[j].getDh().trim().toString())))==1){//����edit by shikun 2014-05-28 ��Ϊ���̫�󣬲�����Integer
								tempvo = gbvos[i];
								gbvos[i] = gbvos[j];
								gbvos[j] = tempvo;
							}
						}
					}
				}
				getBufferData().clear();
				aggvo.setChildrenVO(gbvos);
				getBufferData().addVOToBuffer(aggvo);
				getBufferData().updateView();
				
			}
		}
		return true;
		
	}

	public boolean onBeforeSave(AggregatedValueObject aggvo)throws Exception{
		return onBoSortNo(aggvo);
	}
	/************************add by yhj 2014-03-07 START****************************/
	AggregatedValueObject billvoChangedOld = null;
	public boolean beforeOnSaveSort(){
		try{
		AggregatedValueObject aggvo = getBillCardPanelWrapper().getBillVOFromUI();
//		billvoChangedOld = aggvo;
		if(aggvo != null){
			boolean br = onBeforeSave(aggvo);
			if(!br)
			return false;
		}
		billvoChangedOld = aggvo;
		CircularlyAccessibleValueObject[] cvos = aggvo.getChildrenVO();
		if(cvos != null && cvos.length > 0){
			List list = new ArrayList();
			for (int i = 0; i < cvos.length; i++) {
				list.add(cvos[i].getAttributeValue("dh"));
			}
			if(list != null && list.size() > 0 ){
				BigInteger a1 = BigInteger.valueOf(Long.valueOf(list.get(0).toString()));
				List rowlist = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					a1 = BigInteger.valueOf(Long.valueOf(list.get(i).toString()));
					BigInteger a2 = new BigInteger("0");
					if(i == list.size() - 1){
						 a2 = BigInteger.valueOf(Long.valueOf(list.get(i).toString())) ;
					}else{
						a2 = BigInteger.valueOf(Long.valueOf(list.get(i+1).toString())) ;
					}
					if((a2.subtract(a1)).compareTo(new BigInteger("1"))==1){//edit by shikun ת��BigInteger a2-a1>1
						Integer ds = i + 2;
						rowlist.add(ds);
						
					}
				}
				if(rowlist != null && rowlist.size() > 0){
					StringBuilder str = new StringBuilder();
					for (int j = 0; j < rowlist.size(); j++) {
						if(j == 0){
							str.append(rowlist.get(j));
						}else if(j == rowlist.size() -1 ){
							if(str.toString().endsWith(",")){
								str.append(rowlist.get(j));
							}else{
								str.append(",");
								str.append(rowlist.get(j));
							}
						}else{
							str.append(",");
							str.append(rowlist.get(j));
							str.append(",");
						}
					}
					int ids = MessageDialog.showYesNoDlg(getBillUI(), "����", "�����"+"["+str.toString()+"]"+"�ж�Ų��������Ƿ񱣴棿",MessageDialog.ID_NO);
					if(ids == MessageDialog.ID_NO){
						return false;
					}
				}
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return true;
	}
	/************************add by yhj 2014-03-07 END****************************/
	@SuppressWarnings("null")
	public void onBoSave()
	{
		//У�����Ƿ��ظ���1.У�鵱ǰ���ݶ���Ƿ��ظ���2.У�鵱ǰ�����ж�������ݿ����Ƿ����--wkf---2015-01-08
		//add by zwx 2019-8-15 �ƸǶ�Ų�У��
		if(!(_getCorp().getPk_corp().equals("1078")||_getCorp().getPk_corp().equals("1108"))){
			String[] listdh  = checkDHvo();
			if(listdh != null){
				StringBuffer errors = new StringBuffer();
				for (int i = 0; i < listdh.length-1; i++) {
					errors.append("'"+listdh[i]+"'");
				}
				String errstr = listdh[listdh.length-1];
				errors.append(errstr);
				getBillUI().showErrorMessage("���:\n"+errors);
				return;
			}
		}
		
		//wkf---end---
		//add by zwx 2016-1-14 ��ɽҪ�����ͷ������������߸���������������һ�£�����Ӱ���������
		//������е����߸���������Ĭ����������һ��У��
		if(getUnitPK().equals("1019")){
			if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>1){
				Object xxsl = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "xxaglsl");
				int xxnum = xxsl==null?0:Integer.valueOf(xxsl.toString());
				String zdsl = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValue();
				int zdnum = zdsl==null?0:Integer.valueOf(zdsl);
				if(xxnum!= zdnum){
					getBillUI().showErrorMessage("����/������������������������");
					return;
				}
			}
		}
		//end by zwx 
		if(!beforeOnSaveSort()){
			return;
		}
		
		/**
		 * 1����ȡ���߼��鵥�ϵ�������sjnum
		 * 2����ȡ�����ƻ��ϵ�������jhnum
		 * 3���Ƚ�a��b�Ĵ�С
		 */
		//1����ȡ���߼��鵥�ϵ�������sjnum 
		
		UFDouble sjnum=
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		//2����ȡ�����ƻ��ϵ�������jhnum
		UFDouble jhnum=new UFDouble();
				jhnum=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int bjnum=sjnum.compareTo(jhnum);
		if(bjnum>0){
			getBillUI().showErrorMessage("�����������������ܴ������߼��鵥��������ɵ�����");
		}
		//add by shikun 2014-05-26 ��ǰ����Ķ���δ�깤��������С���㣬���������������겻�ɡ�ddwwgsl
		UFDouble ddwwgsl= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject()==null?new UFDouble(0):
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject().toString());
		if (ddwwgsl.doubleValue()<0) {
			getBillUI().showErrorMessage("��ǰ����Ķ���δ�깤��������С����,���޸ı������������!");
			return;
		}
		//end shikun 2014-05-26
		
		try
		{   
			//�����ʱ���ж϶��ǣ����̣����ֽ�Ƿ�Ϊ�� by src 2018��4��25��19:22:50
			if (ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey().equals("1016")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1071")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1103")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1097")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1017")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1018")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1019")
					|| ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey().equals("1107")) {
				GlBillVO aggvo = (GlBillVO) this.getBillCardPanelWrapper().getBillVOFromUI();
				GlItemBVO[] bvos = (GlItemBVO[]) aggvo.getChildrenVO();
				for(int i = 0; i<bvos.length ;i++){
					String dinggai = bvos[i].getDinggai();
					String tuopan = bvos[i].getTuopan();
					String dianguanzhi = bvos[i].getDianguanzhi();
					if((dinggai==null||dinggai.equals(""))||(tuopan==null||tuopan.equals(""))||(dianguanzhi==null||dianguanzhi.equals(""))){
						MessageDialog.showErrorDlg(getBillUI(), "����Error", "�����"+(i+1)+"�ж��ǣ����̣����ֽ���ڿ�ֵ");
						return;
					}
				}
			}
			GlBillVO billVO = save();
			//�������� add by src 2018��4��13��22:30:26
			String zdsl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValueObject().toString();
			billVO.getParentVO().setAttributeValue("zdsl", zdsl);
			if (billVO != null)
			{
				// ���ý���״̬
				setSaveOperateState();
				// ˢ�½���
				onBoRefresh();

				// �ص��б����
				// onBoReturn();
			}
			//add by src 2018��4��11��16:49:08
//			GlHeadVO hvo = (GlHeadVO) billVO.getParentVO();
//			if(hvo.getPk_corp().equals("1016")){
//				GlItemBVO [] bvo = (GlItemBVO[]) billVO.getChildrenVO();
//				String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//				getBzfs(scddh);
//			}
		} catch (Exception ex)
		{
			Logger.error(ex);
			ex.printStackTrace();
		}

	}
	/*
	 * �˷���Ϊȷ���������ã�����false��true
	 * wkf---2015-01-09
	 * start
	 * */
	@SuppressWarnings("null")
	public boolean onBoSave2()
	{
		//У�����Ƿ��ظ���1.У�鵱ǰ���ݶ���Ƿ��ظ���2.У�鵱ǰ�����ж�������ݿ����Ƿ����--wkf---2015-01-08
		String[] listdh  = checkDHvo();
		if(listdh != null){
			StringBuffer errors = new StringBuffer();
			for (int i = 0; i < listdh.length-1; i++) {
				errors.append("'"+listdh[i]+"'");
			}
			String errstr = listdh[listdh.length-1];
			errors.append(errstr);
			getBillUI().showErrorMessage("���:\n"+errors);
			return false;
		}
		//wkf---end---
		if(!beforeOnSaveSort()){
			return false;
		}
		
		/**
		 * 1����ȡ���߼��鵥�ϵ�������sjnum
		 * 2����ȡ�����ƻ��ϵ�������jhnum
		 * 3���Ƚ�a��b�Ĵ�С
		 */
		//1����ȡ���߼��鵥�ϵ�������sjnum 
		
		UFDouble sjnum=
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		//2����ȡ�����ƻ��ϵ�������jhnum
		UFDouble jhnum=new UFDouble();
				jhnum=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int bjnum=sjnum.compareTo(jhnum);
		if(bjnum>0){
			getBillUI().showErrorMessage("�����������������ܴ������߼��鵥��������ɵ�����");
		}
		//add by shikun 2014-05-26 ��ǰ����Ķ���δ�깤��������С���㣬���������������겻�ɡ�ddwwgsl
		UFDouble ddwwgsl= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject()==null?new UFDouble(0):
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject().toString());
		if (ddwwgsl.doubleValue()<0) {
			getBillUI().showErrorMessage("��ǰ����Ķ���δ�깤��������С����,���޸ı������������!");
			return false;
		}
		//end shikun 2014-05-26
		boolean saveok = false;
		try
		{

			GlBillVO billVO = save();
			if (billVO != null)
			{
				// ���ý���״̬
				setSaveOperateState();
				// ˢ�½���
				onBoRefresh();
				saveok = true;
				// �ص��б����
				// onBoReturn();
			}else{
				saveok = false;
			}
		} catch (Exception ex)
		{
			Logger.error(ex);
			ex.printStackTrace();
		}
		return saveok;

	}
	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
//		//add by src �б��л�����Ƭ�����ǣ����ֽ�����������������ֶε�ֵ�Զ������� 2018��4��11��17:32:57
//		GlBillVO aggvo = (GlBillVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(GlBillVO.class.getName(), GlHeadVO.class.getName(), GlItemBVO.class.getName());
//		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
//        if(hvo.getPk_corp().equals("1016")){
//        	String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//        	getBzfs(scddh);
//		}
	}
	/**
	 * �༭���¼����������������Ŵ��м������ȡ���ݴ�������
	 * by src 2018��4��11��15:40:17
	 * @param scddh
	 * @throws BusinessException
	 */
	public Map getBzfs(String pk_moid) throws BusinessException{
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select dinggai,tuopan,dianguanzhi,def1 from bd_bzfs where pk_moid = '"+pk_moid+"' and pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		List list = (List) uap.executeQuery(sql, new MapListProcessor());
		Map map = null;
		if(list.size()>0){
			map = (Map) list.get(0);
			return map;
//			Object dinggai = map.get("dinggai")==null?"":map.get("dinggai").toString();
//			Object tuopan = map.get("tuopan")==null?"":map.get("tuopan").toString();
//			Object dianguanzhi = map.get("dianguanzhi")==null?"":map.get("dianguanzhi").toString();
//			Object zdsl = map.get("def1");
//			setBodyItemValueDg("dinggai", dinggai);
//			setBodyItemValueTp("tuopan", tuopan);
//			setBodyItemValueDgz("dianguanzhi", dianguanzhi);
//			String zdsl1 = getHeadItem("zdsl").getValue()==null?"":getHeadItem("zdsl").getValue().toString();
//			if(zdsl1.equals("")){
//			   setHeadItemValue("zdsl",zdsl);
//			}
//		}else{
//			setBodyItemValueDg("dinggai", null);
//			setBodyItemValueTp("tuopan", null);
//			setBodyItemValueDgz("dianguanzhi", null);
		}
		return map;
	}
	
	/*//add by src 2018��4��13��13:33:13
	protected void setBodyItemValueDg(String key,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"ľ����","���϶���"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, 0, key);
	}
	//add by src 2018��4��13��13:33:13
	protected void setBodyItemValueTp(String key,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"ľ����","��������","������"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, 0, key);
	}
	//add by src 2018��4��13��13:33:13
	protected void setBodyItemValueDgz(String key,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"��ͨ���ֽ","���ϵ��ֽ"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, 0, key);
	}*/
	//edit by zwx 2019-4-22 �޸��и�Ϊ��̬
	//add by src 2018��4��13��13:33:13
	protected void setBodyItemValueDg(String key,int row,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"ľ����","���϶���"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
	}
	//add by src 2018��4��13��13:33:13
	protected void setBodyItemValueTp(String key,int row,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"ľ����","��������","������"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
	}
	//add by src 2018��4��13��13:33:13
	protected void setBodyItemValueDgz(String key,int row,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"��ͨ���ֽ","���ϵ��ֽ"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
	}
	/**
	 * add by src ���ݶ����ţ������ϵĻ�λ�����ţ���Ŵ�ŵ��м��������ⵥ����
	 * 2018��4��11��15:29:29
	 * @param aggvo
	 * @throws BusinessException
	 */
	private void saveBzfs(GlBillVO aggvo) throws BusinessException{
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		IVOPersistence vo = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
		String sql = "select pk_bzfs from bd_bzfs where pk_moid='"+hvo.getScddh()+"' and pk_corp = '"+hvo.getPk_corp()+"'";
		String sqlscddh = "select scddh from bd_bzfs where pk_moid='"+hvo.getScddh()+"' and pk_corp = '"+hvo.getPk_corp()+"'";
		String pk_bzfs = (String) uap.executeQuery(sql,new ColumnProcessor());
		if(pk_bzfs != null && !pk_bzfs.equals("")){
			String scddh = (String) uap.executeQuery(sqlscddh,new ColumnProcessor());
			GlItemBVO[] bvos = (GlItemBVO[]) aggvo.getChildrenVO();
			List<BzfsBVO> listvo = new ArrayList<BzfsBVO>();
			for(int i = 0 ; i<bvos.length ;i++){
				GlItemBVO bvo = bvos[i];
				BzfsBVO bzfsbvo = new BzfsBVO();
				bzfsbvo.setPk_bzfs(pk_bzfs);
				bzfsbvo.setHuowei(bvo.getHw());//��λ
				bzfsbvo.setHuoweiname(bvo.getHwname());//��λ����
				bzfsbvo.setPihao(bvo.getPh());//����
				bzfsbvo.setDuohao(bvo.getDh());//���
				bzfsbvo.setInvcode(bvo.getLh());//����
				bzfsbvo.setDr(0);
				bzfsbvo.setPk_corp(hvo.getPk_corp());
				bzfsbvo.setCk(hvo.getCk());//�ֿ�
				bzfsbvo.setDinggai(bvo.getDinggai());//����
				bzfsbvo.setTuopan(bvo.getTuopan());//����
				bzfsbvo.setDianguanzhi(bvo.getDianguanzhi());//���ֽ
				bzfsbvo.setScddh(scddh);//����������
				listvo.add(bzfsbvo);
			}
			if(listvo.size()>0){
				vo.insertVOList(listvo);
			}
		}
	}
	
	/*
	 * ���鵱ǰ�����Ƿ����ظ����
	 * wkf
	 * 2015-01-08
	 * */
	@SuppressWarnings({ "null", "unused", "rawtypes" })
	private String[] checkDHvo() {
		
		int indexi = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		String[] dhs = new String[indexi];//��ǰ���ݵ����еĶ��
		for (int j = 0; j < indexi; j++) {
			Object dh = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(j, "dh");
			if(dh != null){
				dhs[j]=dh.toString();
			}
		}
		StringBuffer errors =  new StringBuffer();//��ǰ���ݾ����ظ��Ķ��
		for(int i = 0;i<dhs.length-1;i++){ 
			for(int j = i+1;j<dhs.length;j++){
				if(dhs[i] == dhs[j]){
					errors.append(dhs[i]+",");
				}
			}
		}
		if(errors.length()>0){
			String[] errs= errors.toString().split(",");
			int overii = errs.length;
			String[] errss = new String[overii+1];
			for (int i = 0; i < errs.length; i++) {
				errss[i]=errs[i];
			}
			String errorstr2 = "�ظ�������º����ԣ�";
			errss[overii] = errorstr2;
			return errss;
		}else{
			String pk_corp = getUnitPK();
			StringBuffer sqlwhere = new StringBuffer();
			for (int i = 0; i < dhs.length; i++) {
				sqlwhere.append("'"+dhs[i]+"'");
				if(i<dhs.length-1){
					sqlwhere.append(",");
				}
			}
			Object pk_glzb = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_glzb").getValueObject();
//			String ddsql = "select dh from mm_glzb_b where nvl(dr,0)=0 and (pk_glzb != '"+pk_glzb+"' or '"+pk_glzb+"' is null) and  pk_corp = '"+pk_corp+"' and dh in ("+sqlwhere.toString()+")";
			StringBuffer ddsql = new StringBuffer();
			ddsql.append(" select dh ") 
			.append("   from mm_glzb_b ") 
			.append("  where nvl(dr, 0) = 0 ") 
			.append("    and (pk_glzb != '"+pk_glzb+"' or '"+pk_glzb+"' is null) ") 
			.append("    and pk_corp = '"+pk_corp+"' ") 
			.append("    and dh in ("+sqlwhere.toString()+") ") 
			.append(" union all ") 
			.append(" select hcldh dh ") 
			.append("   from mm_glcl_b ") 
			.append("  where nvl(dr, 0) = 0 ") 
			.append("    and (pk_glzb != '"+pk_glzb+"' or '"+pk_glzb+"' is null) ") 
			.append("    and pk_corp = '"+pk_corp+"' ") 
			.append("    and hcldh in ("+sqlwhere.toString()+") ") ;

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(ddsql.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuffer error2 = new StringBuffer();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map addrMap = (Map) list.get(i);
					String dh = addrMap.get("dh") == null ? "" : addrMap.get("dh").toString();
					error2.append(dh+",");
				}
			}
//			String[] error = new String[list.size()+1];
//				error[overi] = errorstr1;
			
			if(error2.length()>0){
				String[] errs= error2.toString().split(",");
				int overii = errs.length;
				String[] errss = new String[overii+1];
				for (int i = 0; i < errs.length; i++) {
					errss[i]=errs[i];
				}
				String errorstr1 = "��ϵͳ�����ж���ظ�������º����ԣ�";
				errss[overii] = errorstr1;
				return errss;
			}else{
				return null;
			}
		}
	}
	
	
	/**
	 * add by shikun 2014-03-28 
	 * ɾ��ʱ�����ܱ�������/��������xxaglsl������ͷ����δ�깤����ddwwgsl
	 * */
	@Override
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
		bodyToHeadNum();
	}

	/**
	 * add by shikun 2014-03-28 �в���ʱ���ܱ�������/��������xxaglsl������ͷ����δ�깤����ddwwgsl
	 * */
	public void bodyToHeadNum() {
		int ddsl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject()==null?0
                :Integer.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int ddwgsl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject()==null?0
		                  :Integer.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		int ddwwgsl = ddsl-ddwgsl;
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for (int i = 0; i < row; i++) {
			Object oxxaglsl = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "xxaglsl");
			int xxaglsl = oxxaglsl==null?0:Integer.valueOf(oxxaglsl.toString());
			ddwwgsl = ddwwgsl-xxaglsl;
		}
		if (ddwwgsl<0) {
			getBillUI().showWarningMessage("�ѳ����������������޸ı����С�����/������������");
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ddwwgsl", ddwwgsl);
	}

	
	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		//add by src �����ͷûѡ��������������ʾ��ѡ����������
		String scddh = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scddh").getValue()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scddh").getValue().toString();
		if(scddh == null||scddh.equals("")){
			getBillUI().showErrorMessage("��ѡ����������");
			return;
		}
		super.onBoLineAdd();
		//���е�ʱ�����������������ȥ�м������CVM�������İ�װ����Ϣ add by src 2018��4��16��10:00:56
		int num = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		if (ClientEnvironment.getInstance().getCorporation().getPk_corp()
				.equals("1016")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPk_corp().equals("1071")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPk_corp().equals("1103")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1097")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1017")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1018")
			    || ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1019")
				|| ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey().equals("1107")) {
			String dinggai = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "dinggai")==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "dinggai").toString();
			if(dinggai.equals("") && num == 1){
				Map map = getBzfs(scddh);
				if(map ==  null){
					setBodyItemValueDg("dinggai", num-1,"ľ����");
					setBodyItemValueDgz("dianguanzhi",  num-1,"��ͨ���ֽ");
					setBodyItemValueTp("tuopan",  num-1,"ľ����");
				}else{
					String dinggai1 = map.get("dinggai")==null?"":map.get("dinggai").toString();//����
					String tuopan = map.get("tuopan")==null?"":map.get("tuopan").toString();//����
					String dianguanzhi = map.get("dianguanzhi")==null?"":map.get("dianguanzhi").toString();//���ֽ
					setBodyItemValueDg("dinggai",  num-1,dinggai1);
					setBodyItemValueDgz("dianguanzhi",  num-1,dianguanzhi);
					setBodyItemValueTp("tuopan", num-1, tuopan);
				}
			}else{
				Map map = getBzfs(scddh);
				if(map ==  null){
					setBodyItemValueDg("dinggai", num-1, "ľ����");
					setBodyItemValueDgz("dianguanzhi", num-1, "��ͨ���ֽ");
					setBodyItemValueTp("tuopan",  num-1,"ľ����");
				}else{
					String dinggai1 = map.get("dinggai")==null?"":map.get("dinggai").toString();//����
					String tuopan = map.get("tuopan")==null?"":map.get("tuopan").toString();//����
					String dianguanzhi = map.get("dianguanzhi")==null?"":map.get("dianguanzhi").toString();//���ֽ
					setBodyItemValueDg("dinggai", num-1, dinggai1);
					setBodyItemValueDgz("dianguanzhi", num-1, dianguanzhi);
					setBodyItemValueTp("tuopan", num-1, tuopan);
				}
			}
	     }
		//���в���ʱ���ܱ�������/��������xxaglsl������ͷ����δ�깤����ddwwgsl
		bodyToHeadNum();
		//end shikun 
		/**
		 * ����֮��ʱ��У���ͷ������
		 * 1����ȡ���߼��鵥�ϵ�������sjnum
		 * 2����ȡ�����ƻ��ϵ�������jhnum
		 * 3���Ƚ�a��b�Ĵ�С
		 */
		//1����ȡ���߼��鵥�ϵ�������sjnum 
		
		UFDouble sjnum=
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		//2����ȡ�����ƻ��ϵ�������jhnum
		UFDouble jhnum=new UFDouble();
				jhnum=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int bjnum=sjnum.compareTo(jhnum);
		if(bjnum>0){
			getBillUI().showErrorMessage("�����������������ܴ������߼��鵥��������ɵ�����");
		}
	}
	protected boolean checkCofirmVO(GlBillVO billVO)
	{
		// GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();
		if (head.getDjzt() == 1)
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "�����Ѵ���Documents have been processed!");
			return false;
		}
		if (head.getCk() == null)
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "�ֿⲻ��Ϊ�գ�The warehouse not as empty!");
			return false;
		}
		if (head.getJyjg() != 0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "ֻ�кϸ�ĵ��ݲ���Ҫȷ�ϣ�Only qualified documents need to confirm!");
			return false;
		}
		if (head.getZdsl() == null || head.getZdsl() == 0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "����Error", "������������Ϊ�գ�The entire stack Quantity can not be empty!");
			return false;
		}
		
		return true;
	}

	protected boolean checkVO(GlBillVO billVO) throws Exception
	{ 
		GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();

		if (head.getScddh() == null || head.getScddh().trim().equals(""))
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "����Error", "���������Ų���Ϊ�գ�Production order number can not be empty!");
			return false;
		}
		
		List<String> tempDh = new ArrayList<String>();
		//StringBuffer sqlWhere = new StringBuffer(" dh in('");
		StringBuffer errdh=new StringBuffer();
		int i=0;
		for (GlItemBVO temp : vos)
		{
			
			//MessageDialog.showErrorDlg(null,"���߸���",String.valueOf(temp.hw.length()));
			
			if (head.getJyjg() == 1
					&& (temp.getGlyy() == null || temp.getGlyy().trim().equals(
							"")))
			{

				MessageDialog.showErrorDlg(this.getBillUI(), "����Error",
						"������Ϊ���룬�����������ԭ��The test results for the isolated isolation reasons, you must enter!");
				
				setColumn(i, "glyy");
				
				return false;
			}
			
			if(temp.getDh()==null||temp.getDh().trim().equals(""))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "����Error",
				"��Ų���Ϊ�գ�Stack number can not be empty!");
				
				setColumn(i, "dh");
				
				return false;
			}
			if(tempDh.contains(temp.getDh()))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "����Error",
						Transformations.getLstrFromMuiStr(new StringBuilder("���@Stack number&[").append(temp.getDh().trim()).append("]&�ظ���@repeats!").toString()));
				
				setColumn(i, "dh");
				
				return false;
			}
			if(temp.getXxaglsl()==null||temp.getXxaglsl()==0)
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "����Error",
				"����\\������������Ϊ�գ�The downline \\ isolate the number can not be empty!");
				
				setColumn(i, "xxaglsl");
				
				return false;
			}
			else if(!chekDh(temp.getDh()))
			{
				errdh.append("��["+(i+1)+"]��:["+temp.getDh()+"]\r\n");
			}
			tempDh.add(temp.getDh());
			//sqlWhere.append(temp.getDh()).append("','");
			
			i++;
		}
		if(errdh.length()>0)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "����Error","���ֲ�����Ҫ��Ķ��@Does not meet the requirements crib No.&��\r\n"+errdh.toString());
			return false;
		}
//		sqlWhere = sqlWhere.delete(sqlWhere.length()-2, sqlWhere.length());
//		sqlWhere.append(")");
//		if(head.getPk_glzb()!=null&&!head.getPk_glzb().trim().equals(""))
//		{
//			sqlWhere.append(" and pk_glzb!='").append(head.getPk_glzb().trim()).append("' ");
//		}
//		//У���Ų����ظ�
//		GlItemBVO[] items = (GlItemBVO[])HYPubBO_Client.queryByCondition(GlItemBVO.class,sqlWhere.toString());
//		if(items!=null&&items.length>0)
//		{
//			MessageDialog.showErrorDlg(this.getBillUI(), "����Error",
//			"���������ݶ���ظ���");
//			return false;
//		}
		
		/*
		 *ע�� ��������ݿ��ж��У���ظ��������淽����ͷ���ʴ˴�ע�� wkf---2015-01-09
		 *  
		i=0;
		for (GlItemBVO temp : vos)
		{
			StringBuilder sql =null;
			if(temp.getStatus()==VOStatus.NEW)
			{
				  sql=new StringBuilder("select dh from mm_glzb_b where nvl(dr,0)=0 and dh ='").append(temp.getDh().trim()+"' ");
				
			}
			else
			{
				sql=new StringBuilder("select dh from mm_glzb_b where nvl(dr,0)=0 and dh ='").append(temp.getDh().trim()).append("' and pk_glzb_b!='"+temp.getPrimaryKey()+"' ");
			}
				sql.append(" union all select hcldh from mm_glcl_b where nvl(dr,0)=0 and hcldh ='").append(temp.getDh().trim()).append("'");
			
			List voList = null;
			try {
				IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				voList = (List)uAPQueryBS.executeQuery(sql.toString(), new ArrayListProcessor());
			} catch (Exception e1) {
				MessageDialog.showErrorDlg(this.getBillUI(),"����Error",e1.getMessage());
				return false;
			}
			if(voList!=null&&voList.size()>0)
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "����Error",
				new StringBuilder("���@Stack number&[").append(temp.getDh().trim()).append("]�Ѵ��ڣ�@repeats!").toString());
				
				setColumn(i, "dh");
			
				return false;
			}			
			
			i++;
		}
		*/
		return true;
	}

	// protected boolean checkVO(GlBillVO billVO)
	// {
	// boolean flag = false;
	// GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
	// GlHeadVO head = (GlHeadVO) billVO.getParentVO();
	// return flag;
	// }

	protected BillItem getHeadBillItem(String key)
	{
		return this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				key);
	}

	protected void initialAdd()
	{
		setHeadItemValue("xxrq", ClientEnvironment.getServerTime().getDate());
		setHeadItemValue("phrq", getPihaoDate());
		setHeadItemValue("jyjg", 0);
		setHeadItemValue("djzt", 0);
		setHeadItemValue("ck",getDeafultCk());
	}
	
	protected String getDeafultCk()
	{
		String ck = null;
		try
		{
			ck = (String) HYPubBO_Client.findColValue("bd_stordoc", "pk_stordoc", " storcode = 'B01' and pk_corp='"+getUnitPK()+"'");
		} catch (UifException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ck;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception
	{
		switch (intBtn)
		{
		case IButton.Confirm: // ȷ��
			this.onConfirm();
			break;
		case IButton.Update_Num: // ���Ļ�λ
			onUpdateNum();
			break;
		case IButton.Print:
			onBoPrint();
			break;
			
			/** 2019-07-31
			 * ���ű�
			 */
		case IButton.Offline://����
			IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
			String corp = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_corp").getValue();
			Boolean result = idetermineService.check(corp);
			if(result){
				onOffline();
			}
			break;
			//add by yhj 2014-03-11
//		case IButton.SORT_NO://�������
//			onBoSortNo();
//			break;
			//end
		default:

		}
	}

//	protected GeneralBillVO getRkVO(GlBillVO billVO) throws UifException
//	{
//		GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
//		GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
//
//		GeneralBillVO bo = new GeneralBillVO();
//		GeneralBillItemVO[] itemvos = new GeneralBillItemVO[vos.length];
//		bo.setLockOperatorid(ghead.getLrr());
//
//		// CLASTMODIID
//		GeneralBillHeaderVO head = bo.getHeaderVO();
//		head.setStatus(2);// ����
//		bo.setParentVO(head);
//		bo.setChildrenVO(itemvos);
//
//		head.setCbilltypecode("46"); // ��浥�����ͱ��� 46-����Ʒ��ⵥ
//		head.setCbizid(ghead.getXxry()); // ҵ��ԱID
//		head.setCdispatcherid("1085A210000000001P2B"); // �շ�����ID ? ������Ʒ��⡱
//														// �ڲ���Ʒ������ɲ���
//		// head.setCdptid(ghead.getBz()); //����ID ��
//		// head.setCgeneralhid(ghead.getPk_glzb()); //����
//		head.setCoperatorid(ghead.getLrr());// �Ƶ���
//		head.setCwarehouseid(ghead.getCk());// �ֿ�ID
//		// head.setCwhsmanagerid(null);//���Ա
//		head.setDbilldate(ClientEnvironment.getServerTime().getDate());// ��������
//		head.setFbillflag(null);// ����״̬
//		// head.setPk_calbody();//�����֯PK
//		head.setPk_corp(this.getUnitPK());// ��˾ID
//		head.setVbillcode(null); // ���ݺ�
//		head.setVnote("���߸��뵥����");// ��ע
//
//		for (int i = 0; i < vos.length; i++)
//		{
//			itemvos[i] = new GeneralBillItemVO();
//			itemvos[i].setStatus(2);// ����;
//			// ���� BONROADFLAG BTOINZGFLAG BTOINZGFLAG BTOOUTTOIAFLAG
//			itemvos[i].setBbarcodeclose(new UFBoolean(false)); // �������Ƿ�����ر�
//			itemvos[i].setBreturnprofit(new UFBoolean(false));// �Ƿ���
//			itemvos[i].setBsafeprice(new UFBoolean(false));// �Ƿ�۱�
//			itemvos[i].setBsourcelargess(new UFBoolean(false));// �����Ƿ���Ʒ��
//			itemvos[i].setBzgflag(new UFBoolean(false));// �ݹ���־
//			// itemvos[i].setCfirstbillbid(vos[i].getPk_glzb_b());//Դͷ���ݱ���id
//			// itemvos[i].setCfirstbillhid(vos[i].getPk_glzb());//Դͷ���ݱ�ͷid
//			// itemvos[i].setCfirsttype("JYDJ");//Դͷ��������
//			// itemvos[i].setCfreezeid(vos[i].getPk_glzb_b());//������Դ
//			itemvos[i].setCgeneralbid(null); // ����������������ֵ
//			itemvos[i].setCgeneralhid(null); // ��ͷ������������ֵΪ��
//
//			String pk_invbasdoc = (String) HYPubBO_Client.findColValue(
//					"bd_invmandoc", "pk_invbasdoc", " pk_invmandoc='"
//							+ ghead.getCp() + "'");
//			itemvos[i].setCinvbasid(pk_invbasdoc);// �������id
//
//			itemvos[i].setCinventoryid(ghead.getCp());// ���id
//			itemvos[i].setCrowno(((i + 1) * 10) + "");// �к�
//			// itemvos[i].setCsourcebillbid(vos[i].getPk_glzb_b());//��Դ���ݱ������к�
//			// itemvos[i].setCsourcebillhid(vos[i].getPk_glzb());//��Դ���ݱ�ͷ���к�
//			// itemvos[i].setCsourcetype("JYDJ");//��Դ��������
//			itemvos[i].setCvendorid(ghead.getKs());// ��Ӧ��id ����������ɶ����
//			itemvos[i].setDbizdate(ClientEnvironment.getInstance().getDate());// ҵ������
//			// itemvos[i].setDbizdate(null);//ҵ������
//			itemvos[i].setFchecked(0);// 0 ������
//			itemvos[i].setFlargess(new UFBoolean(false)); // N �Ƿ���Ʒ
//			itemvos[i].setIsok(new UFBoolean(true));// Y
//			itemvos[i].setNbarcodenum(null);// 0 ��������
//			// itemvos[i].setNinnum(new
//			// UFDouble(vos[i].getXxaglsl().doubleValue()));//ʵ������
//			itemvos[i].setNmny(null);// ���
//			itemvos[i].setNprice(null);// ����
//			itemvos[i].setNshouldinnum(null);// Ӧ������
//			// itemvos[i].setPk_corp();
//			// itemvos[i].setPK_INVOICECORP();
//			// itemvos[i].setPK_REQCORP();
//			itemvos[i].setVbatchcode(null);// ���κ� ��ⵥ����ɲ���
//			itemvos[i].setVfirstbillcode(null);// Դͷ���ݺ�
//			itemvos[i].setVproductbatch(null);// �������ţ�
//			itemvos[i].setVsourcebillcode(null);// ��Դ���ݺ�
//			// itemvos[i].setVsourcerowno(i+"");// ��Դ�����к�
//			itemvos[i].setVuserdef1(null);//
//			itemvos[i].setPk_cubasdoc(ghead.getKs());// ���̻������� ����Ӧ����ɶ����
//		}
//		return bo;
//	}
	
	/**
	 * ���߼��鵥��ͷ������ʼ��š����߶����������߰�ť���Զ����ɱ�����ϸ��
	* 2019-08-01
	* @author ���ű�
	*/
	protected void onOffline(){
		   try{
		      String ds=null;//����Ķ���ֶ�
		      Integer jg=null;//��λתΪ���ִ���Ľ��
		      String dh=null;//���еĶ��
		      List<String> list=new ArrayList<String>();
		       UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		       BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		       int curRow = table.getRowCount();
		       /*
		        * �����еĶ�ŷŵ�������
		        */
		       for(int k=0;k<curRow;k++){
		    	  dh=(String)this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(k,"dh");
		    	 list.add(dh);
		       }
		       UIRefPane sdhpane = (UIRefPane) getHeadBillItem("startdh").getComponent();
		       String startdh=sdhpane.getRefName();//��ȡ��ʼ��ŵ�ֵ
		       UIRefPane xxdspane = (UIRefPane) getHeadBillItem("xxds").getComponent();
		       String xxds=xxdspane.getRefName();//��ȡ���߶�����ֵ
		       if("".equals(startdh.trim())){
		    	   getBillUI().showErrorMessage("��ʼ��Ų���Ϊ��");
		    	   return;
		       }
		        if("".equals(xxds.trim())){
		        	getBillUI().showErrorMessage("���߶�������Ϊ��");
		        	return;
		        }
		        
		        String endfour=StringUtils.substring(startdh.trim(), -4);
		        if("0".equals(StringUtils.substring(endfour, -4,-3))){//�жϽ�ȡ�ĵ���λ�Ƿ�Ϊ0�����жϺ���λ��Ӷ����Ƿ���Ҫ��λ
		        Integer d=Integer.parseInt(StringUtils.substring(endfour,-3))+Integer.parseInt(xxds.trim());
		        	if(d.toString().length()>StringUtils.substring(endfour,-3).length()){
		        		getBillUI().showErrorMessage("Ԥ���ֶγ��ȳ���");
		        		return; 
		        	}
		        }else{
		        	Integer ko=Integer.parseInt(endfour)+Integer.parseInt(xxds.trim());
				       if(ko.toString().length()>endfour.length()){
				    	   getBillUI().showErrorMessage("Ԥ���ֶγ��ȳ���");
				    	   return; 
				       }
		        }
		       UIRefPane pane = (UIRefPane) getHeadBillItem("cp").getComponent();  
		       UIRefPane hwpane = (UIRefPane) getHeadBillItem("pghw").getComponent();
		       UIRefPane ckpane = (UIRefPane) getHeadBillItem("ck").getComponent();
		       if(("".equals(hwpane.getRefName())) || (null==hwpane.getRefName())){
		    	   getBillUI().showErrorMessage("���Ļ�λ����Ϊ��");
		    	   return;
		       }
		       if(("".equals(ckpane.getRefName()))|| (null==ckpane.getRefName())){
		    	   getBillUI().showErrorMessage("�ֿⲻ��Ϊ��");
		    	   return;
		       }
		       for(int i=0;i<Integer.parseInt(xxds.trim());i++){
		    	   int j=i+curRow;//�����Ķ�ŵ��к�
		    	   if("0".equals(StringUtils.substring(endfour, -4,-3))){//�жϽ�ȡ�ĵ���λ�Ƿ�Ϊ0,�ǣ�����Ӧ��λ�������
		    		   jg=Integer.parseInt(StringUtils.substring(startdh.trim(), -4))+i;
			    	   ds=StringUtils.substring(startdh.trim(), 0,startdh.trim().length()-4)+"0"+jg.toString();
		    	   }else{
		    		   jg=Integer.parseInt(StringUtils.substring(startdh.trim(), -4))+i;
			    	   ds=StringUtils.substring(startdh.trim(), 0,startdh.trim().length()-4)+jg.toString();
		    	   }
		             /*
		              * �ж϶���Ƿ��ظ�
		              */
		             if(list.contains(ds)){
		            	 getBillUI().showErrorMessage("��Ų����ظ�");
		            	 return;
		             }
		             onBoLineAdd();
		             model.setValueAt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValue(), j, "xxaglsl");
		 			 model.setValueAt(getUnitPK(), j, "pk_corp");
		             model.setValueAt(pane.getRefValue("bd_invbasdoc.invcode"), j,"lh"); 
		             model.setValueAt(pane.getRefName(), j, "cp");
		             model.setValueAt(hwpane.getRefValue("bd_cargdoc.cscode "), j, "hw");
		             model.setValueAt(ds, j, "dh");
		             this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(j, "lh");
		              
		               }                    
		           }catch(Exception ex){
		               nc.bs.logging.Logger.error(ex);
		               ex.printStackTrace();
		           }                      
		       			        			        
			    }

	protected void onUpdateNum()
	{
		UIRefPane pane = (UIRefPane) getHeadItem("pghw").getComponent();

		UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable();
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();
		int[] rows = table.getSelectedRows();

		for (int row : rows)
		{
			model.setValueAt(pane.getRefCode(), row, "hw");
			model.setValueAt(pane.getRefName(), row, "hwmc");
			model.setValueAt(pane.getRefPK(), row, "cspaceid");
			model.setRowState(row, 2); //������Ϊ�Ѹ���״̬
		}

	}

	protected BillItem getHeadItem(String key)
	{
		return this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				key);
	}

	/**
	 * ��ȡ������ʱ��
	 * 
	 * @return
	 */
	protected UFDate getPihaoDate()
	{
		UFDateTime date = ClientEnvironment.getServerTime();
		if (date.getTime().compareTo("08:30") < 0)
		{
			return ClientEnvironment.getServerTime().getDateBefore(1).getDate();
		} else
		{
			return ClientEnvironment.getServerTime().getDate();
		}
	}

	protected void setHeadItemValue(String key, Object value)
	{
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(key)
				.setValue(value);
	}
	

//	�������뽹��
	private void setColumn(int row, String col)
	{
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		//BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		
		String colName = this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem(col).getName();
		
		int colIndex = table.getColumnModel().getColumnIndex(colName);
		
		table.setRowSelectionInterval(row, row);
		table.setColumnSelectionInterval(colIndex, colIndex);
		table.editCellAt(row, colIndex);
		
		table.setEditingRow(row);
		table.setEditingColumn(colIndex);
	}
	
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)throws Exception
	{
		if(sqlWhereBuf == null)
        throw new IllegalArgumentException("askForQueryCondition().sqlWhereBuf cann't be null");
        UIDialog querydialog = getQueryUI();
         if(querydialog.showModal() != 1)
        return false;
    INormalQuery query = (INormalQuery)querydialog;
    String strWhere = query.getWhereSql();
    if(strWhere == null)
        strWhere = "1=1";
    if(getButtonManager().getButton(2) != null)
        if(getBillIsUseBusiCode().booleanValue())
            strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and ").append(getBillField().getField_BusiCode()).append("='").append(getBillUI().getBusicode()).append("'").toString();
        else
            strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and ").append(getBillField().getField_Busitype()).append("='").append(getBillUI().getBusinessType()).append("'").toString();
    strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and (nvl(mm_glzb.dr,0)=0)").toString();
     if(getHeadCondition() != null)
        strWhere = (new StringBuilder()).append(strWhere).append(" and ").append("mm_glzb."+getHeadCondition()).toString();
     sqlWhereBuf.append(strWhere);
      return true;
	}
	private boolean chekDh(String dh)
	  {
		 if(dh==null||dh.equals("")||dh.equalsIgnoreCase("null"))
		 {
			 return  false;
		 }
		 else if( dh.length()!=10)
		 {
//			 return false;
			 if(!(_getCorp().getPk_corp().equals("1078")||_getCorp().getPk_corp().equals("1108"))){//add by zwx 2019-8-15 �Ƹǲ�У��
				 return false;
			 }else{
				 return true;
			 }
		 }
		 else 
		 {
			 Pattern pattern = Pattern.compile("\\d{10}");
			 Matcher matcher = pattern.matcher(dh);
			return  matcher.matches();
		 }
	  }
   private boolean CheckNumberOfShipments(String moid,int currQty)
   {
	   StringBuffer sql=new StringBuffer("select a.scddh, sum(nvl(b.xxaglsl, 0.0)) as CompletedQty from mm_glzb a, mm_glzb_b b where nvl(b.dr, 0) = 0 ");
	   sql.append("and nvl(a.djzt, 0) = 1 ").append("and a.pk_glzb = b.pk_glzb ")
	   .append("and a.billsign='JYDJ' and nvl(scddh,'')='"+moid+"'").append(" and nvl(a.jyjg,0)=0  group by a.scddh");
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
		HashMap hm = null;
		int totalnum=0;
	try {
		hm = (HashMap) sessionManager.executeQuery(sql.toString(),
			new MapProcessor());

	if (hm==null||hm.isEmpty()) {
		return true;
	}
	else 
	{
		String comnum=String.valueOf(hm.get("completedqty"));
		comnum=comnum==null||comnum.equals("")||comnum.equalsIgnoreCase("null")?"0":comnum;
		totalnum=currQty+new BigDecimal(comnum).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		sql.setLength(0);
		sql.append("select nvl(jhwgsl,0.0)*1.01 as plannum from mm_mo where pk_moid='"+moid+"'");
		hm.clear();
		hm = (HashMap) sessionManager.executeQuery(sql.toString(),
				new MapProcessor());
		String plannum=String.valueOf(hm.get("plannum"));
		if(plannum==null||plannum.equals("")||plannum.equalsIgnoreCase("null"))
		{
			MessageDialog.showErrorDlg(getBillUI(),"����Error","���������ļƻ���������Ϊ�գ�Planned production number of the production order is empty!");
			return false;
		}
		else 
		{
			if(totalnum>new BigDecimal(plannum).setScale(0, BigDecimal.ROUND_HALF_UP).intValue())
			{
				MessageDialog.showErrorDlg(getBillUI(),"����Error","���������Ѿ��������������ļƻ�����������1%��Referral number has exceeded the planned production number of the production order 1%!");
				return false;
			}
		}
	}
	}catch(BusinessException e)
	{
		MessageDialog.showErrorDlg(getBillUI(),"����Error",e.getMessage());
		return false;
	}
	return true; 
   }
 
}