package nc.ui.pub.pf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.ic.pub.pf.QrySaleBillForInvoiceDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.so.pub.FetchValueBO_Client;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 单据来源 公共参照对话框
 *
 * @author 樊冠军 2001-7-6
 * @modifier 雷军 2005-2-22 开放getUIDialogContentPane()方法,便于多态
 * @modifier leijun 2006-7-4 适应V5单据模板的修改，废弃一些无用方法
 */
public class BillSourceDLG extends AbstractReferQueryUI implements ActionListener,
		BillEditListener, BillTableMouseListener, ListSelectionListener {
	protected BillListPanel ivjbillListPanel = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjbtnCancel = null;

	private UIButton ivjbtnOk = null;

	private UIButton ivjbtnQuery = null;
	
	private UIButton ivjbtnTotal = null;
	
	private UIButton ivjbtnCancelTotal = null;

	private UIPanel ivjPanlCmd = null;

	//单据vo,主表vo,子表vo
	protected String m_billVo = null;

	protected String m_billHeadVo = null;

	protected String m_billBodyVo = null;

	//查询条件语句
	protected String m_whereStr = null;

	//返回的集合Vo
	protected AggregatedValueObject retBillVo = null;

	//返回集合VO数组
	protected AggregatedValueObject[] retBillVos = null;

	protected boolean isRelationCorp = true;

	//主计量数量小数位数
	protected Integer m_BD501 = null;

	//辅计量数量小数位数
	protected Integer m_BD502 = null;

	//单价小数位数
	protected Integer m_BD505 = null;

	//换算率小数位数
	protected Integer m_BD503 = null;

	/**
	 * 根据类名称，where语句构造参照界面
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param parent
	 */
	public BillSourceDLG(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, java.awt.Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}

	/**
	 * 根据类名称，where语句构造参照界面
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 */
	public BillSourceDLG(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, String nodeKey, Object userObj, java.awt.Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, nodeKey, userObj, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getbtnOk()) {
			onOk();
		} else if (e.getSource() == getbtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getbtnQuery()) {
			onQuery();
		} else if (e.getSource() == getbtnSelect()) {
			onSelectAll();
		}else if (e.getSource() == getbtnCancelSelect()) {
			onCancelSelectAll();
		}
	}

	/**
	 * 增加单据模版
	 * <li>该方法被PfUtilClient.childButtonClicked()调用
	 */
	public void addBillUI() {
		//增加模版调用
		getUIDialogContentPane().add(getbillListPanel(), "Center");
		//增加对控件监听
		addListenerEvent();
	}

	/**
	 * 事件侦听
	 */
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getbtnQuery().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addMouseListener(this);
		getbtnSelect().addActionListener(this);
		getbtnCancelSelect().addActionListener(this);

		//表头列表 行切换事件处理器
		getbillListPanel().getParentListPanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	/* (non-Javadoc)
	 * @see BillEditListener#afterEdit(BillEditEvent)
	 */
	public void afterEdit(BillEditEvent e) {
	}
	
	public void onSelectAll(){
		BillModel headModel = getbillListPanel().getHeadBillModel();
		int row = getbillListPanel().getHeadTable().getRowCount();
		for(int i =0 ; i<row ;i++){
			if(headModel.getRowState(i) != BillModel.SELECTED)
				headRowDoubleClicked(i);
		}
	}
	
	public void onCancelSelectAll(){
		BillModel headModel = getbillListPanel().getHeadBillModel();
		int row = getbillListPanel().getHeadTable().getRowCount();
		for(int i =0 ; i<row ;i++){
			if(headModel.getRowState(i) == BillModel.SELECTED)
				headRowDoubleClicked(i);
		}
	}

	/**
	 * 只对表头进行处理
	 * <li>行切换 事件
	 * <li>双击 事件
	 * <li>WARN::行切换事件发生在双击事件之前
	 * @param iNewRow
	 */
	private synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow, getpkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				//1.初次载入表体数据
				loadBodyData(iNewRow);
				//2.备份到模型中
				getbillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getbillListPanel().repaint();
	}

	/* (non-Javadoc)
	 * @see BillEditListener#bodyRowChange(BillEditEvent)
	 */
	public void bodyRowChange(BillEditEvent e) {
	}

	/**
	 * @return
	 */
	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				//获的显示位数值
				//装载模板
				nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(getBillType(), null,
				/*getBusinessType(),*/getOperator(), getPkCorp(), getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				//更改主表的显示位数
				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				//更改子表的显示位数
				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				//进行主子隐藏列的判断
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				ivjbillListPanel.setMultiSelect(true);
			} catch (java.lang.Throwable ivjExc) {
				ivjExc.printStackTrace();
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * 获得单据类型VO类信息
	 *
	 * <li>数组[0]=单据聚合Vo;数组[1]=单据主表Vo;数组[2]=单据子表Vo;
	 */
	public void getBillVO() {
		try {
			String[] retString = PfUIDataCache.getStrBillVo(getBillType());
			//MatchTableBO_Client.querybillVo(getBillType());
			//0--单据vo;1-主表Vo;2-子表Vo;
			m_billVo = retString[0];
			m_billHeadVo = retString[1];
			m_billBodyVo = retString[2];
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 子表条件语句
	 * @return
	 */
	public String getBodyCondition() {
		return null;
	}

	/**
	 * 子表隐藏字段
	 * @return
	 */
	public String[] getBodyHideCol() {
		return null;
	}

	/**
	 * 返回子表二维数组，第一维为必须为2。第二维不限，
	 * 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}}
	 * 注意:必须保证二行的长度相等。否则系统按默认值2取。
	 * 创建日期：(2001-12-25 10:19:03)
	 * @return java.lang.String[][]
	 */
	public String[][] getBodyShowNum() {
		return null;
	}

	private nc.ui.pub.beans.UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new nc.ui.pub.beans.UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
		}
		return ivjbtnCancel;
	}

	private nc.ui.pub.beans.UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new nc.ui.pub.beans.UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);
		}
		return ivjbtnOk;
	}

	private nc.ui.pub.beans.UIButton getbtnQuery() {
		if (ivjbtnQuery == null) {
			ivjbtnQuery = new nc.ui.pub.beans.UIButton();
			ivjbtnQuery.setName("btnQuery");
			ivjbtnQuery
					.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*@res "查询"*/);
		}
		return ivjbtnQuery;
	}

	//eric
	private nc.ui.pub.beans.UIButton getbtnSelect() {
		if (ivjbtnTotal == null) {
			ivjbtnTotal = new nc.ui.pub.beans.UIButton();
			ivjbtnTotal.setName("ivjbtnTotal");
			ivjbtnTotal.setText("全选");
		}
		return ivjbtnTotal;
	}
	
	private nc.ui.pub.beans.UIButton getbtnCancelSelect() {
		if (ivjbtnCancelTotal == null) {
			ivjbtnCancelTotal = new nc.ui.pub.beans.UIButton();
			ivjbtnCancelTotal.setName("ivjbtnCancelTotal");
			ivjbtnCancelTotal.setText("全消");
		}
		return ivjbtnCancelTotal;
	}
	/**
	 * 主表查询条件
	 * @return
	 */
	public String getHeadCondition() {
		return null;
	}

	/**
	 * 主表隐藏字段
	 * @return
	 */
	public String[] getHeadHideCol() {
		return null;
	}

	/**
	 * 返回主表二维数组，第一维为必须为2。第二维不限，
	 * 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}}
	 * 注意:必须保证二行的长度相等。否则系统按默认值2取。
	 * 创建日期：(2001-12-25 10:19:03)
	 * @return java.lang.String[][]
	 */
	public String[][] getHeadShowNum() {
		return null;
	}

	/**
	 * 返回是否与业务类型有关，
	 * 如果与业务类型无关，则系统不进行业务类型条件的拼接
	 * 注意:必须明确保证是否与业务类型的关系
	 * @return
	 */
	public boolean getIsBusinessType() {
		return true;
	}

	private nc.ui.pub.beans.UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new nc.ui.pub.beans.UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new java.awt.Dimension(0, 40));
			ivjPanlCmd.setLayout(new java.awt.FlowLayout());
			ivjPanlCmd.add(getbtnSelect(), getbtnSelect().getName());
			ivjPanlCmd.add(getbtnCancelSelect(), getbtnCancelSelect().getName());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			ivjPanlCmd.add(getbtnQuery(), getbtnQuery().getName());
		}
		return ivjPanlCmd;
	}

	public AggregatedValueObject getRetVo() {
		return retBillVo;
	}

	public AggregatedValueObject[] getRetVos() {
		return retBillVos;
	}

	protected javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setSize(1200, 600);
			ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
			//2003-05-12平台进行显示调用
			//getUIDialogContentPane().add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), "South");
		}
		return ivjUIDialogContentPane;
	}

	private void initialize() {

		setName("BillSourceUI");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		// add by zip:2013/12/18 pub
		setResizable(true);
		// end
		setSize(1200, 600);//edit by shikun 2014-03-19 
		setTitle(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000114")/*@res "单据的参照界面"*/);
		setContentPane(getUIDialogContentPane());

		//*****************I******************************
		//2003-05-04该处定义为需函数，由平台进行显示调用

		//增加监听事件
		//addListenerEvent();
		//读取数据
		//loadHeadData();
		//************************************************
		//获取单据对应的单据vo名称
		getBillVO();
		// user code end
	}

	/**
	 * 根据主表获取子表数据
	 * @param row 选中的表头行
	 */
	@SuppressWarnings("restriction")
	public void loadBodyData(int row) {
		try {
			//获得主表ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
			//查询子表VO数组
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData(getBillType(),
					id, getBodyCondition());
			/**
			 *根据输入的存货编码条件，过滤掉同单据的其他料号数据
			 *作者：王凯飞
			 *start
			 *2013-11-27
			 **/
			String s = m_whereStr;//取到查询语句的条件
			String[] sr=s.split("and");//用and将语句分割
			String s1=null;
			for (int i = 0; i < sr.length; i++) {//for循环取出分割出的语句装到s1中
				if(sr[i].toString().indexOf("invcode")!=-1){
					s1 = sr[i];
				}
			}
			CircularlyAccessibleValueObject[] tmpBodyVo1 = new CircularlyAccessibleValueObject[tmpBodyVo.length];//从tmpBodyVo中取出数据装入tmpBodyVo1中
			if(s1!=null){//判断s1是否为空
				int i1 = s1.indexOf("2");//取得料号一个数字2所在位数
				String s2 =  s1.substring(i1, s1.length()-2);//从数字2处截取料号
				String[] pk_invbasdocs = null;
				try {
					pk_invbasdocs = FetchValueBO_Client.getColValues("bd_invbasdoc inv ",
							"pk_invbasdoc", s1 );
				} catch (Exception e) {
					e.printStackTrace();
				}
				int rows = 0;
				if (pk_invbasdocs!=null&&pk_invbasdocs.length>0) {
					for (int j = 0; j < pk_invbasdocs.length; j++) {
						String pk_invbasdoc = pk_invbasdocs[j];
						for (int i = 0; i < tmpBodyVo.length; i++) {
							GeneralBillItemVO gbivo = new GeneralBillItemVO();
							gbivo = (GeneralBillItemVO)tmpBodyVo[i];
							if(gbivo.getCinvbasid().equals(pk_invbasdoc)){
								tmpBodyVo1[rows] = tmpBodyVo[i];
								rows++;
							}
						}
					}
				}
				if(rows>0){
					CircularlyAccessibleValueObject[] tmpBodyVo2 = new CircularlyAccessibleValueObject[rows];
					int rows1 = 0;
					for (int i = 0; i < tmpBodyVo1.length; i++) {
						if(tmpBodyVo1[i]!=null){
							tmpBodyVo2[rows1] = tmpBodyVo1[i];
							rows1++;
						}
					}
					getbillListPanel().setBodyValueVO(tmpBodyVo2);
				}
			}else{//若不为空则直接装入值
				getbillListPanel().setBodyValueVO(tmpBodyVo);
			}
			/**
			 *根据输入的存货编码条件，过滤掉同单据的其他料号数据
			 *作者：王凯飞
			 *end
			 *2013-11-27
			 **/
//			getbillListPanel().setBodyValueVO(tmpBodyVo);//update by wkf 2013-11-28 
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			System.out.println("数据加载失败!");
			e.printStackTrace(System.out);
		}

	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.pf.AbstractReferQueryUI#loadHeadData()
	 */
	public void loadHeadData() {
		try {
			//利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.queryHeadAllData(getBillType(),
					businessType, tmpWhere);

			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000490")/*@res "数据加载失败！"*/);
		}
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.bill.BillTableMouseListener#mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent)
	 */
	public void mouse_doubleclick(BillMouseEnent e) {
		if (e.getPos() == BillItem.HEAD) {
			//WARN::只对表头的双击事件进行响应,表体的双击事件在BillListPanel.BodyMouseListener中响应
			final int headRow = e.getRow();

			// leijun 2006-7-4 耗时操作，显示等待对话框
			new Thread(new Runnable() {
				public void run() {
					BannerDialog dialog = new BannerDialog(BillSourceDLG.this);
					// FIXME::i18n
					dialog.setStartText(nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000491")/*@res "获取数据中，请稍等..."*/);
					try {
						dialog.start();

						headRowDoubleClicked(headRow);
					} finally {
						dialog.end();
					}
				}

			}).start();

		}
	}

	private void headRowDoubleClicked(int headRow) {
		//行切换
		headRowChange(headRow);

		BillModel bodyModel = getbillListPanel().getBodyBillModel();
		BillModel headModel = getbillListPanel().getHeadBillModel();
		//选取处理
		if (getbillListPanel().isMultiSelect()) {
			if (headModel.getRowState(headRow) == BillModel.SELECTED) {
				headModel.setRowState(headRow, BillModel.UNSTATE);
				for (int i = 0; i < bodyModel.getRowCount(); i++)
					bodyModel.setRowState(i, BillModel.UNSTATE);
			} else {
				headModel.setRowState(headRow, BillModel.SELECTED);
				for (int i = 0; i < bodyModel.getRowCount(); i++)
					bodyModel.setRowState(i, BillModel.SELECTED);
			}
			//备份数据
			getbillListPanel().setBodyModelDataCopy(headRow);
		}
	}

	/**
	 * “确定”按钮的响应，从界面获取被选单据VO
	 * @modifier leijun 2005-12-20 改用自己的方法从单据UI获取VO数组
	 * @modifier leijun 2006-05-29 单据模板的选取有改动，还是调用它们的吧
	 */
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {

			//leijun@2006-5-29
			AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
					m_billHeadVo, m_billBodyVo);
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
		}
		this.closeOK();
	}

	/**
	 * 在该界面上进行再次查询
	 */
	public void onQuery() {
		QueryConditionClient queryCondition = getQueyDlg();
		queryCondition.showModal();
		if (queryCondition.isCloseOK()) {
			//返回查询条件
			m_whereStr = queryCondition.getWhereSQL();
			loadHeadData();
			//fgj@ 2001-11-06 修改置空表体数据
			getbillListPanel().setBodyValueVO(null);
			//hxr@ 2005-3-31 初始选中一行
			JTable table = getbillListPanel().getParentListPanel().getTable();
			int iRowCount = table.getRowCount();
			if (iRowCount > 0 && table.getSelectedRow() < 0) {
				table.changeSelection(0, 0, false, false);
			}
		}
	}

	/**
	 * 更改主表的显示位数（根据产品的返回）
	 */
	public void setVoDecimalDigitsBody(BillListData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2) { return; }
		if (strShow[0].length != strShow[1].length) { throw new Exception(NCLangRes.getInstance()
				.getStrByID("102220", "UPP102220-000115")/*@res "显示位数组第一、二行不匹配"*/); }
		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getBodyItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/**
	 * 更改主表的显示位数（根据产品的返回）
	 */
	public void setVoDecimalDigitsHead(BillListData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2) { return; }
		if (strShow[0].length != strShow[1].length) { throw new Exception(NCLangRes.getInstance()
				.getStrByID("102220", "UPP102220-000115")/*@res "显示位数组第一、二行不匹配"*/); }
		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = billDataVo.getHeadItem(attrName);
			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int headRow = ((javax.swing.ListSelectionModel) e.getSource()).getAnchorSelectionIndex();
			if (headRow >= 0) {
				headRowChange(headRow);
			}
		}
	}
}