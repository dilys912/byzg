package nc.ui.pi.invoice;

///////JAVA
import java.util.*;
//////VO
import nc.vo.pi.*;
import nc.vo.pub.query.*;
import nc.vo.pub.util.*;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pub.SCMEnv;
//////UI
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.*;
import nc.ui.pi.pub.*;
import nc.ui.bd.ref.*;
import nc.ui.pub.pf.*;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.MultiCorpQueryClient;

/**
 * ��ⵥ->��Ʊ�Ĳ�ѯ��
 * ���ߣ���ӡ��
 * @version   ����޸�����
 * @see         ��Ҫ�μ���������
 * @since		�Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ��
 */
public class InvFrmStoQueDlg extends MultiCorpQueryClient implements IinitQueryData{
  //���⹫˾����
  public final static String CORPKEY = "ic_general_corp";

  /*
   * ��˾�����ĵ���
   * ע�⣬
   * ���⣬�ͻ�������ҵ������ ��UAP��δ֧������Ȩ��
   * 
   * ���š���Ա�������֯���ֿ�(ic_general_h.cwarehouseid)��ȡ������Ȩ�޿��� 20070413
   * 
  */
   public final static String[] REFKEYS = new String[]{
//     "bd_deptdoc.deptcode",//���ŵ���,
//     "ic_general_h.cdptid",//���ŵ���,
//     "ic_general_h.cwarehouseid",//�ֿ⵵��,
     "ic_general_h.cproviderid",//��Ӧ�̵���,
     "po_order.cgiveinvoicevendor",
     "o_order_b.cprojectid",//��Ŀ
     "bd_invcl.invclasscode",//�������
     "bd_invbasdoc.invcode"//�������
//     "bd_psndoc.psncode",//��Ա����,
//     "ic_general_h.cbizid",//��Ա����,
//     "ic_general_h.cwhsmanagerid",//��Ա����,
   };

	//��ǰҵ������
	private java.lang.String m_strBizType;
	//��Դ��������
	private java.lang.String m_strBillType;
/**
 * InvFrmStoQueDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public InvFrmStoQueDlg(
	java.awt.Container parent) {

	super(parent);
  // Ȩ�޿��ƣ����ò��պ͹�˾��ϵ SINCE V5.1
  initCorpRefs();

}
/**
 * InvFrmOrdQueDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public InvFrmStoQueDlg(
	java.awt.Container parent,
	String pk_corp,
	String operator,
	String nodecode,
	String strBizType,
	String strCurrBillType,
	String strBillType,
	Object obUser) {

	super(parent);
	setBizType(strBizType);
	setBillType(strBillType);
	m_pkCorp = pk_corp;
	m_operator = operator;
	m_sourceBillType = strBillType;

	try {
		initData(
			pk_corp,
			operator,
			nodecode,
			strBizType,
			strCurrBillType,
			strBillType,
			obUser);

	} catch (Exception e) {
		SCMEnv.out(e);

	}
  // Ȩ�޿��ƣ����ò��պ͹�˾��ϵ SINCE V5.1
  initCorpRefs();

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-28 9:43:30)
 * @return java.lang.String
 */
private java.lang.String getBillType() {
	return m_strBillType;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-28 9:43:30)
 * @return java.lang.String
 */
private java.lang.String getBizType() {
	return m_strBizType;
}
/**
 * ���ݲ�ѯ��ĳ��������õ��������������ַ���
 * @param          ����˵��
 * @return          ����ֵ
 * @exception    �쳣����
 * @see              ��Ҫ�μ�����������
 * @since            �������һ���汾���˷�������ӽ���������ѡ��
 *
 */

//��ʱȡ�����иõ�λ�ķ�Ʊ,��ͬʱ��ʾ��һ��
private NormalCondVO[] getNormalCondVOs() {
	//��Ʊ���ڣ���Ӧ�̣���������ţ�ҵ��Ա,������࣬��������
	//�Ƿ��ڳ����Ƿ��������Ƿ����

	//��ѯ����VO
	NormalCondVO[]	vos = new	NormalCondVO[3] ;
	vos[0] = new	NormalCondVO("��˾",getPk_corp()) ;
	vos[1] = new	NormalCondVO("ҵ������",getBizType()) ;
	vos[2] = new	NormalCondVO("��������",getBillType()) ;

	return vos ;
}
/**
 * �õ���˾��������Ĭ�ϲ���ʹ�á�
 * �������ڣ�(2001-8-17 11:17:03)
 * @return java.lang.String
 */
private String getPk_corp() {
	return ClientEnvironment.getInstance().getCorporation().getPk_corp();
}
/**
 * �õ�������ⵥ���ɷ�Ʊ�Ĳ�ѯ����
 *  @param          ����˵��
 *  @return          ����ֵ
 *  @exception    �쳣����
 *  @see              ��Ҫ�μ�����������
 *  @since            �������һ���汾���˷�������ӽ���
 * @return java.lang.String
 */


 //ע����Ϊ��Դ����Ϊ�����������ϲ���Դ��ʲô��
private String getPoStoWhereSQL(
	NormalCondVO[]	normalVOs,
	ConditionVO[] definedVOs
	) {

	//1ɾ���Ĳ�����ѯ
	String condStr =
						//��ⵥ
						" (ic_general_h.fbillflag = 3 OR ic_general_h.fbillflag = 4) "
					+	" AND ic_general_h.dr=0"
					+	" AND ic_general_b.dr=0"
						//��Ʒ�������
					+   " AND ISNULL(ic_general_b.flargess,'N')='N'"
					+	" AND ic_general_bb3.dr=0"
					    //�ɹ���ⵥ��ȫ����㲻�ܲ���
					+   " AND ic_general_b.isok = 'N'"
						//��������
					+	" AND ic_general_b.ninnum IS NOT NULL ";
//					+	" AND ("
//					+	"		ic_general_bb3.nsignnum IS NULL "
//					+	"		OR ABS(ic_general_b.ninnum)>ABS(ic_general_bb3.nsignnum) "
//					+	"	   )" ;

	for (int i = 0; i < normalVOs.length; i++){
		if(normalVOs[i].getKey().equals("��˾")){
			condStr += " AND " + normalVOs[i].getWhereStr("ic_general_b.pk_invoicecorp","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("ҵ������")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbiztype","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("��������")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbilltypecode","=",NormalCondVO.STRING) ;
		}
	}

	/*�������	��ⵥ��	������	��Ӧ��	���		�ɹ�����		ҵ��Ա*/
	//��ر�VO
	RelatedTableVO	tableVO = new	RelatedTableVO("��ⵥ") ;
	//�Զ���
	String	strDefined = "", sTemp = "", s = "";
	int k1 = 0, k2 = 0;
	//////////////////���Զ����������д���
	if(definedVOs!=null && definedVOs.length!=0){
		//�����������Զ�������ΪAND��ϵ
		for (int i = 0; i < definedVOs.length; i++){
      //�Ѵ�ӡ�������ϱ�����������ѯʱ����������ȷ��SQL�쳣
      if("iprintcount".equals(definedVOs[i].getFieldCode())){
        definedVOs[i].setFieldCode("ic_general_h.iprintcount");
      }else if("ISNULL(iprintcount,0)".equals(definedVOs[i].getFieldCode())){
        definedVOs[i].setFieldCode("ISNULL(ic_general_h.iprintcount,0)");
      }
			//�õ���VO�Ķ�Ӧ���
			sTemp = definedVOs[i].getSQLStr();
			if(sTemp != null && sTemp.indexOf("AND bd_cumandoc.pk_corp") >= 0){
				k1 = sTemp.indexOf("AND bd_cumandoc.pk_corp");
				k2 = sTemp.indexOf(")", k1);
				s = sTemp.substring(k1, k2);
				if(s != null) sTemp = sTemp.replaceAll(s, "");
			}
			if(sTemp != null && sTemp.indexOf("AND bd_invmandoc.pk_corp") >= 0){
				k1 = sTemp.indexOf("AND bd_invmandoc.pk_corp");
				k2 = sTemp.indexOf(")", k1);
				s = sTemp.substring(k1, k2);
				if(s != null) sTemp = sTemp.replaceAll(s, "");
			}

      if(sTemp != null && sTemp.indexOf("ic_general_h.cproviderid in ( (select areaclcode from bd_areacl where 0=0  and pk_areacl") >= 0){
        sTemp = sTemp.replace("ic_general_h.cproviderid in ( (select areaclcode from bd_areacl where 0=0  and pk_areacl", "ic_general_h.cproviderid in ( (select pk_cumandoc from bd_cubasdoc,bd_cumandoc,bd_areacl where bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc and bd_cubasdoc.pk_areacl = bd_areacl.pk_areacl and bd_areacl.pk_areacl");
      }
			strDefined += sTemp;
			if(i==0){
				strDefined = strDefined.substring(strDefined.indexOf("(", 1));
			}
			///�õ�����
			int	index = definedVOs[i].getFieldCode().indexOf(".",1) ;
      
      //�����ַ��������쳣
      if(index >= 0){
        String  table = definedVOs[i].getFieldCode().substring(0,index) ;
        if( !table.equals("ic_general_h") && !table.equals("po_order") ){
          String  describtion = RelatedTableVO.getJoinTableDescription(table) ;
          if(describtion!=null)
            tableVO.addElement(describtion) ;
        }
      }
		}
		strDefined = " AND (" + strDefined + ")" ;
	}
	condStr += strDefined ;

	//�˵����Ĵ��
	tableVO.addElement("���������") ;
	condStr += " AND bd_invmandoc.sealflag='N'" ;

	String	strBaseTableJoin = 		tableVO.getBaseTableJoin()
			//add by danxionghui NO 20
			  //������
			+"LEFT OUTER JOIN po_arriveorder "
			+"ON  po_arriveorder.carriveorderid = ic_general_b.csourcebillhid"
			////end  by danxionghui NO20
								//����
								+ " LEFT OUTER JOIN po_order_b "
								+ "      ON     ic_general_b.cfirstbillhid = po_order_b.corderid"
								+ "         AND ic_general_b.cfirstbillbid = po_order_b.corder_bid"
								+ " LEFT OUTER JOIN po_order "
								+ "      ON     po_order.corderid=po_order_b.corderid " ;
	if( !tableVO.containTable("��Ӧ�̻�������") ){
		strBaseTableJoin += " LEFT OUTER JOIN bd_cumandoc ON ic_general_h.cproviderid=bd_cumandoc.pk_cumandoc " ;
	}

	tableVO.setBaseTableJoin(strBaseTableJoin) ;
	strBaseTableJoin = tableVO.getFromTable() ;

	condStr += 	  " AND ("
				+ "         (ic_general_b.cfirsttype IS NULL) OR"
				+ "         ( "
				+ "                 po_order.dr=0 "
				+ "             AND po_order_b.dr=0 "
				+ "             AND ic_general_b.cfirsttype = '"+ nc.vo.scm.pu.BillTypeConst.PO_ORDER + "'"
				+ "          )"
				+ "     )"
				+ " AND (bd_cumandoc.frozenflag IS NULL OR bd_cumandoc.frozenflag='N')" ;

	strBaseTableJoin = "FROM " + strBaseTableJoin + " WHERE " + condStr ;
  
	return strBaseTableJoin ;
}
/**
 * �õ�������ⵥ���ɷ�Ʊ�Ĳ�ѯ����
 *  @param          ����˵��
 *  @return          ����ֵ
 *  @exception    �쳣����
 *  @see              ��Ҫ�μ�����������
 *  @since            �������һ���汾���˷�������ӽ���
 * @return java.lang.String
 */


 //ע����Ϊ��Դ����Ϊ�����������ϲ���Դ��ʲô��
private String getScStoWhereSQL(
	NormalCondVO[]	normalVOs,
	ConditionVO[] definedVOs
	) {

	//1ɾ���Ĳ�����ѯ
	String condStr = 	//��ⵥ
						" (ic_general_h.fbillflag = 3 OR ic_general_h.fbillflag = 4) "
					+	" AND ic_general_h.dr=0"
					+	" AND ic_general_b.dr=0"
						//��Ʒ�������
					+   " AND ISNULL(ic_general_b.flargess,'N')='N'"
					+	" AND ic_general_bb3.dr=0"
						//��������
					+	" AND ic_general_b.ninnum IS NOT NULL ";
//					+	" AND (	"
//					+	"		ic_general_bb3.nsignnum IS NULL "
//					+	"		OR ABS(ic_general_b.ninnum)>ABS(ic_general_bb3.nsignnum)"
//					+	"	   )"
//					+	" AND ic_general_b.bzgflag='Y' " ;//edit by shikun 2015-01-22
	if (!("1078".equals(m_pkCorp)||"1108".equals(m_pkCorp))) {//���Ƹǹ�˾edit by shikun 2015-01-22
		condStr = condStr + " AND ic_general_b.bzgflag='Y' " ;
	}else{//�Ƹǹ�˾--�ǿ��ί��ӹ���ⵥedit by shikun 2015-01-22
		if(!getBillType().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)){
			condStr = condStr + " AND ic_general_b.bzgflag='Y' " ;
		}
	}

	for (int i = 0; i < normalVOs.length; i++){
		if(normalVOs[i].getKey().equals("��˾")){
			condStr += " AND " + normalVOs[i].getWhereStr("ic_general_h.pk_corp","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("ҵ������")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbiztype","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("��������")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbilltypecode","=",NormalCondVO.STRING) ;
		}
	}

	/*�������	��ⵥ��	������	��Ӧ��	���		�ɹ�����		ҵ��Ա*/
	//��ر�VO
	RelatedTableVO	tableVO = new	RelatedTableVO("��ⵥ") ;
	//�Զ���
	String	strDefined = "" ;
	//////////////////���Զ����������д���
	if(definedVOs!=null && definedVOs.length!=0){
		//�����������Զ�������ΪAND��ϵ
		for (int i = 0; i < definedVOs.length; i++){
			//�õ���VO�Ķ�Ӧ���
			strDefined += definedVOs[i].getSQLStr() ;
			if(i==0){
				strDefined = strDefined.substring(strDefined.indexOf("(", 1));
			}
			///�õ�����
			int	index = definedVOs[i].getFieldCode().indexOf(".",1) ;
			String	table = definedVOs[i].getFieldCode().substring(0,index) ;
			if( !table.equals("ic_general_h") && !table.equals("po_order") ){
				String	describtion = RelatedTableVO.getJoinTableDescription(table) ;
				if(describtion!=null)
					tableVO.addElement(describtion) ;
			}
		}
		strDefined = " AND (" + strDefined + ")" ;
	}
	condStr += strDefined ;

	//�˵����Ĵ��
	tableVO.addElement("���������") ;
	condStr += " AND bd_invmandoc.sealflag='N'" ;

	String	strBaseTableJoin = 		tableVO.getBaseTableJoin()
								//����
								+ " LEFT OUTER JOIN sc_order_b "
								+ "      ON     ic_general_b.cfirstbillhid = sc_order_b.corderid"
								+ "         AND ic_general_b.cfirstbillbid = sc_order_b.corder_bid"
								+ " LEFT OUTER JOIN sc_order "
								+ "      ON     sc_order.corderid=sc_order_b.corderid " ;
	if( !tableVO.containTable("��Ӧ�̻�������") ){
		strBaseTableJoin += " LEFT OUTER JOIN bd_cumandoc ON ic_general_h.cproviderid=bd_cumandoc.pk_cumandoc " ;
	}
	tableVO.setBaseTableJoin(strBaseTableJoin) ;
	strBaseTableJoin = tableVO.getFromTable() ;

	condStr += 	  " AND ("
				+ "         (ic_general_b.cfirsttype IS NULL) OR"
				+ "         ( "
				+ "                 sc_order.dr=0 "
				+ "             AND sc_order_b.dr=0 "
				+ "             AND ic_general_b.cfirsttype = '"+ nc.vo.scm.pu.BillTypeConst.SC_ORDER + "'"
				+ "          )"
				+ "     )"
				+ " AND (bd_cumandoc.frozenflag IS NULL OR bd_cumandoc.frozenflag='N')" ;

	strBaseTableJoin = "FROM " + strBaseTableJoin + " WHERE " + condStr ;

	//�����йزɹ������Ĳ�ѯ�������滻Ϊί�ⶩ���Ĳ�ѯ����
	strBaseTableJoin = StringUtil.replaceAllString(strBaseTableJoin,"po_order","sc_order") ;
	//����
	return strBaseTableJoin ;
}
/**
 * �����ConditionVO[]��ϳɵ�where�Ӿ䡣
 * �������ڣ�(2001-7-12 16:09:26)
 * @return java.lang.String
 * 2002-11-14	wyf		�����LIKE����
 */
public String getWhereSQL() {

	//��ѯ����VO
	NormalCondVO[]	normalVOs = getNormalCondVOs() ;
	ConditionVO[]	definedVOs = getConditionVO() ;
	definedVOs = dealAreaForVendor(definedVOs,this.getPk_corp());

	if(definedVOs != null && definedVOs.length >0)
		definedVOs = nc.ui.scm.pub.query.ConvertQueryCondition.getConvertedVO(definedVOs,getPk_corp());

	//wyf	2002-11-14	add	begin
	PiPqPublicUIClass.processLIKEInCondVOs(definedVOs) ;
	//wyf	2002-11-14	add	end

	if(getBillType().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)){
		return	getScStoWhereSQL(normalVOs,definedVOs) ;
	}else{
		return	getPoStoWhereSQL(normalVOs,definedVOs) ;
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-12-1 12:19:04)
 */
public void initData(
	String pkCorp,
	String operator,
	String funNode,
	String businessType,
	String currentBillType,
	String sourceBilltype,
	Object userObj)
	throws Exception {

	//�����õ��ı���
	setBillType(sourceBilltype) ;
	setBizType(businessType) ;
	m_pkCorp = pkCorp;
	m_operator = operator;
	m_sourceBillType = sourceBilltype;


	initiDefine() ;
	initiNormal() ;
	initiTitle() ;
	initiMultiUnit() ;
	//��ѯģ�����Ӵ�ӡ����
	setShowPrintStatusPanel(true);

	nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
		this,
		getPk_corp(),
    "icbill",
		"ic_general.vuserdef",
		"ic_general_b.vuserdef");
	nc.ui.scm.pub.def.DefSetTool.updateQueryConditionForCubasdoc(
		this,
		getPk_corp(),
		"bd_cubasdoc.def"
		);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initiDefine() {
	//���س�������
	hideNormal() ;


}
/**
 * ���ߣ���ӡ��
 * ���ܣ���ʼ���Զ���ģ��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-10-13 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2002-12-02	wyf		�޸Ĵ������Ӧ�̵ķ�����״����
 */
private void initiNormal() {

	//װ��ģ��
	//setTempletID("40040401030000000000") ;
	String s = null;
	if(m_sourceBillType.equals("45")){//���ɹ���ⵥ
		m_funNode = "40089910";
		s = "40080602";
	}else if(m_sourceBillType.equals("47")){//���ί��ӹ���ⵥ
		m_funNode = "40089911";
		s = "40080606";
	}else if(m_sourceBillType.equals("4T")){//����ڳ��ɹ�
		m_funNode = "40089912";
		s = "40080402";
	}
	setTempletID(m_pkCorp, s, m_operator, m_strBizType,m_funNode);


	//�����Զ���ġ������š�
	//UIRefPane	pane1 = new	UIRefPane() ;
	//pane1.setIsCustomDefined(true) ;
	//AbstractRefModel	model1 = null ;
	//if(getBillType().equals(nc.vo.pu.pub.BillTypeConst.STORE_SC)){
		//model1 = new	VOrderCodeRefModel(getPk_corp(),getBizType(),nc.vo.pu.pub.BillTypeConst.SC_ORDER) ;
		//model1.addWherePart(" AND sc_order.ibillstatus=3") ;
	//}else{
		//model1 = new	VOrderCodeRefModel(getPk_corp(),getBizType(),nc.vo.pu.pub.BillTypeConst.PO_ORDER) ;
		//model1.addWherePart(" AND po_order.forderstatus IN (3,5,7)") ;
	//}
	//pane1.setRefModel(model1) ;
	//setValueRef("po_order.vordercode", pane1);

	//����ⵥ�š�����
	//AbstractRefModel	model2 = new	VGeneralCodeRefModel(getPk_corp(),getBizType(),getBillType()) ;
	//model2.addWherePart(" AND ic_general_h.cregister IS NOT NULL") ;
	//UIRefPane	pane2 = new	UIRefPane() ;
	//pane2.setIsCustomDefined(true) ;
	//pane2.setRefModel(model2) ;
	//setValueRef("ic_general_h.vbillcode", pane2);
	//�˵����Ĵ�������ɲɹ����
	//AbstractRefModel	model3 = new	nc.ui.bd.ref.DefaultRefModel("�������") ;
	DefaultRefGridTreeModel	model3 = new	DefaultRefGridTreeModel("�������") ;
	model3.addWherePart(" AND bd_invmandoc.sealflag='N' AND UPPER(ISNULL(bd_invmandoc.iscanpurchased,'Y')) = 'Y'") ;
	UIRefPane	pane3 = new	UIRefPane() ;
	//pane3.setIsCustomDefined(true) ;
	pane3.setRefModel(model3) ;
	setValueRef("bd_invbasdoc.invcode", pane3);

	//�˵�����Ĺ�Ӧ��
	//AbstractRefModel	model4 = new	nc.ui.bd.ref.DefaultRefModel("��Ӧ�̵���") ;
	DefaultRefGridTreeModel	model4 = new	DefaultRefGridTreeModel("��Ӧ�̵���") ;
	model4.addWherePart(" AND bd_cumandoc.frozenflag='N'") ;
	UIRefPane	pane4 = new	UIRefPane() ;
	//pane4.setIsCustomDefined(true) ;
	pane4.setRefModel(model4) ;
	setValueRef("bd_cubasdoc.custcode", pane4);

	setValueRef("ic_general_h.dbilldate","����");
	setDefaultValue("ic_general_h.dbilldate","ic_general_h.dbilldate",nc.ui.pub.ClientEnvironment.getInstance().getDate().toString());

}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initiTitle() {

	setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000126")/*@res "������ⵥ���ɷ�Ʊ"*/);
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-28 9:43:30)
 * @param newBizType java.lang.String
 */
private void setBillType(java.lang.String newBillType) {
	m_strBillType = newBillType;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-28 9:43:30)
 * @param newBizType java.lang.String
 */
private void setBizType(java.lang.String newBizType) {
	m_strBizType = newBizType;
}

	//�ڵ����
	String m_funNode = null;
	//����Ա
	String m_operator = null;
	//��˾����
	String m_pkCorp = null;
	//��ǰ��������
	String m_sourceBillType = null;

/**
 * �˴����뷽��˵����
 * ��������:�����ѯ�����İ�����ϵ
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:
 * @return nc.vo.pub.query.ConditionVO[]
 */
private ConditionVO[] dealAreaForVendor(ConditionVO[] cons, String pk_corp) {
	/*************************************************************/
	//��������:( (bd_areacl.areaclcode = '01') or (bd_areacl.areaclcode = '0301') or
	//(bd_areacl.areaclcode = '030101') or (bd_areacl.areaclcode = '030101') )
	//������
	/**************************************************************/
	try {
		Vector v = new Vector();
		for (int i = 0; cons != null && i < cons.length; i++) {

			//����Ӧ�����ڵ�����ѯ����
			// modified by wwm on 2003-09-19
			if(cons[i].getTableCodeForMultiTable() == null
				|| !cons[i].getTableCodeForMultiTable().trim().equalsIgnoreCase("areaclcode")){
				v.add(cons[i]);
				continue;
			}
			//if (cons[i].getTableCodeForMultiTable() != null
				//&& !cons[i].getTableCodeForMultiTable().trim().equalsIgnoreCase("areaclcode")) {
				//v.add(cons[i]);

			// end
			else {

				String[] areaCodes =
					nc.ui.pu.pub.PubHelper.getSubAreaCodes(
						pk_corp,
						cons[i].getValue(),
						cons[i].getOperaCode());
				for (int j = 0; j < areaCodes.length; j++) {

					ConditionVO con = (ConditionVO) cons[i].clone();
					if (j == 0) {
						con.setNoLeft(false);
					} else {
						con.setNoLeft(true);
					}
					con.setOperaCode("=");
					con.setValue(areaCodes[j]);

					if (j == areaCodes.length - 1) {
						con.setNoRight(false);
					} else {
						con.setNoRight(true);
					}

					if (j > 0) {
						con.setLogic(false);
					}
					v.add(con);

				}
			}

		}

		ConditionVO[] vos = null;
		if (v.size() > 0) {
			vos = new ConditionVO[v.size()];
			v.copyInto(vos);
		}
		return vos;
	} catch (Exception e) {
		SCMEnv.out(e);
		return cons;
	}
}

/**
 * ����"�൥λѡ��"��ť
 * �������ڣ�(2001-12-1 12:19:04)
 */
private void initiMultiUnit()
{
	hideUnitButton() ;
}

/**
 * ��ѯģ����û�й�˾ʱ��Ҫ�������⹫˾��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author lxd
 * @time 2007-3-13 ����11:10:56
 */
public void initCorpRefs(){

  ArrayList<String> alcorp=new ArrayList<String>();
  alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
  initCorpRef(CORPKEY,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),alcorp);   
  //
  setDefaultCorps(new String[] {ClientEnvironment.getInstance().getCorporation().getPrimaryKey()});
  //
  setCorpRefs(CORPKEY, REFKEYS);
  //    
}
}