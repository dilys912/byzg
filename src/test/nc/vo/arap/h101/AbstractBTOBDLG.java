package nc.vo.arap.h101;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
/**
 * ˵��: ������������ε��ݵ��Զ��������
 * @author ������
 * 2007-10-22 ����02:31:11
 */
public abstract class AbstractBTOBDLG extends BillSourceDLG {
    
    /**�б�ģ��*/
    protected nc.ui.pub.bill.BillListPanel m_arrListPanel     = null;
    /**�б�����*/
    protected java.awt.Container           m_parent           = null;
    /**��ѯ��*/
    protected AbstractBTOBBillQueryDLG     queryCondition     = null;
    /**����VO[]*/
    protected AggregatedValueObject[]      m_Vos              = null;
    

//    public AbstractBTOBDLG() {
//        super();
//    }

    public AbstractBTOBDLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, parent);
    }

    public AbstractBTOBDLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);       
    }
    /**
     * ����: ���ñ�ͷ������
     * @return
     * @author ������
     * 2007-10-22 ����02:45:53
     */
    public String[] getHeadHideCol() {
        return new String[] { "" };
    }
    
    /**
     * ����: ���ñ���������
     * @return
     * @author ������
     * 2007-10-22 ����02:45:53
     */
    public String[] getBodyHideCol() {
        return null;
    }
    /**
     * ����: ��ͷ��ѯ���� 
     * @return
     * @author ������
     * 2007-10-22 ����02:46:16
     */
    public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
//        strWherePart.append(" pk_busitype ='");
//        strWherePart.append(getBusinessType());
//        strWherePart.append("' and vbilltype='");
//        strWherePart.append(getBillType());
//        strWherePart.append("' and pk_corp = '");
//        strWherePart.append(getPkCorp());
//        strWherePart.append("' and vbillstatus =");
//        strWherePart.append(ISHSHBillStatus.CHECKPASS);
//        strWherePart.append("' ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /** 
     * ����: ���ñ����ѯ����
     * @return String
     * @author ������
     * 2007-10-22 ����02:45:53
     */
    public String getBodyCondition() {
        return null;
    }
    /**
     * ����:  �����ѯ���淽��
     * @return
     * @return:AbstractBTOBBillQueryDLG
     * @author ������
     * 2007-10-22 ����04:47:18
     */
    protected abstract AbstractBTOBBillQueryDLG getQueryDlg();
    /**
     * ����: ��ѯ����
     * @author ������
     * 2007-10-22 ����04:54:49
     */
    public void onQuery() {
        getQueryDlg().showModal();
        if (getQueryDlg().isCloseOK()) {
            // ���ز�ѯ����
            m_whereStr = getQueryDlg().getWhereSQL();
            loadHeadData();
        }
    }   
    /**
     * ����:  ��ȡ����VO����
     * @return: String[]
     * @author ������
     * 2007-10-22 ����04:48:09
     */
    protected abstract String[] getBillVos();
    
    /**
     * ����: ���ر�ͷ����
     * @param row
     * @author ������
     * 2007-10-22 ����04:47:37
     */
    public void loadHeadData() {
        try {
            // ���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
            String tmpWhere = null;
            if (!isEmpty(getHeadCondition())) {
                if (m_whereStr == null) {
                    tmpWhere = " (" + getHeadCondition() + ") order by dmakedate desc";
                } else {
                    tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ") ";
                }
            } else {
                if (m_whereStr == null) {
                    tmpWhere = " 1=1 ";
                } else {
                    tmpWhere = " (" + m_whereStr + ") ";
                }
            }
            //��ȥ��ͷVO
            String BillVos = getBillVos()[1];
            SuperVO[] parentvo = (SuperVO[]) HYPubBO_Client.queryByCondition(Class.forName(BillVos), tmpWhere);
            //�Ѳ�ѯ����VO����Ž�����VO m_Vos��
            if(!isEmpty(parentvo)){
                m_Vos= new AggregatedValueObject[parentvo.length];
                for (int i = 0; i < m_Vos.length; i++) {
                    m_Vos[i]=new HYBillVO();
                    m_Vos[i].setParentVO(parentvo[i]);
                }

                getbillListPanel().setHeaderValueVO(parentvo);
                getbillListPanel().getHeadBillModel().execLoadFormula(); // ��֤��ʽ
                getbillListPanel().getHeadTable().clearSelection();
            }
        } catch (Exception e) {
            System.out.println("���ݼ���ʧ�ܣ�");
            e.printStackTrace(System.out);
        }

    }
	/**
     * ����: ���ر�������
     * @param row
     * @author ������
     * 2007-10-22 ����04:47:37
     */
    public void loadBodyData(int row) {
        try {

            if (m_Vos == null || m_Vos.length < row + 1) {
                System.out.println("����BUG����ͷ��ǰѡ���г�����ͷ����!");
                return;
            }
            // ��ѯ�������ݲ��ٲ�ѯ
            SuperVO[] tmpBodyVo = null;
            if (m_Vos[row].getChildrenVO() == null || m_Vos[row].getChildrenVO().length == 0) {
                String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField())
                        .toString();
                if (id == null) {
                    System.out.println("����BUG����ͷID��TS������ȷ��ȡ!");
                    return;
                }
                tmpBodyVo = (SuperVO[]) m_Vos[row].getChildrenVO();
                m_Vos[row].setChildrenVO(tmpBodyVo);
            }
            //��ȡ������Ӧ�е�����
            String pk = m_Vos[row].getParentVO().getPrimaryKey().toString();
            String tmpWhere = null;
            if (getBodyCondition() != null && getBodyCondition().length() > 0) {
                if (m_whereStr == null) {
                    tmpWhere = " (" + getBodyCondition() + ")";
                }
//                else {
//                    tmpWhere = " (" + m_whereStr + ") and (" + getBodyCondition() + ")";
//                }
                else {
                  tmpWhere = " (" + getBodyCondition() + ")";
              }
            } else {
                if (m_whereStr == null) {
                    tmpWhere = " 1=1 "; 
                }
            }
            if(tmpWhere==null){
                tmpWhere=" 1=1 ";
            }
            if(pk.length()>0){
                tmpWhere += " and "+getpkField()+" = '"+pk+"'";
            }
            //��ȥ���յ����б���VO
            String BillVos = getBillVos()[2];
            //���ظ���������ѯ�ı���
            SuperVO[] childvo = (SuperVO[]) HYPubBO_Client.queryByCondition(Class.forName(BillVos), tmpWhere);
            getbillListPanel().setBodyValueVO(childvo);
            getbillListPanel().getBodyBillModel().execLoadFormula();

        } catch (Exception e) {
            e.printStackTrace(System.out);
            if (e instanceof java.rmi.RemoteException)
                nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "���ر���", e.getMessage());
        }
    } 


    /**
     * ����: ȷ����ť�����ʱ�ж��Ƿ�ѡ���� 
     *      ����ѡ��Ϊ��ѡʱ���������ӱ�VO����һ�������ﷵ��
     *      ���� retBillVo
     * @author ������
     * 2007-10-22 ����03:14:05
     */
    public void onOk() {
       
        //��û�����ε��ݿ��Բ���ʱ ������ʾ
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"û�п��Բ��յĵ���!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
 
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel()
                    .setBodyModelDataCopy(
                            getbillListPanel().getHeadBillModel().convertIntoModelRow(
                                    getbillListPanel().getHeadTable().getSelectedRow()));
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            if(retBillVo.getChildrenVO()==null||retBillVo.getChildrenVO().length==0){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {
                    list.add(childs[j]);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
    public AggregatedValueObject getRetVo() {
        return retBillVo;
    }
    /**
     * ����: ���õ�ѡʱ����getRetVo()
     * @return AggregatedValueObject[]
     * @author ������
     * 2007-10-24 ����03:18:48
     */
    public AggregatedValueObject[] getRetVos() {
        
        AggregatedValueObject[] agg = {getRetVo()};
        return agg;
    }

    /**
     * ����: ���ظ��෽����ʵ�ֲ�ͬ��ҵ�����͵��ò�ͬ�Ĳ�ѯģ��
     * @return
     * @author ������
     * 2007-10-22 ����02:32:54
     */
    protected nc.ui.pub.bill.BillListPanel getbillListPanel() {
        if (ivjbillListPanel == null) {
            try {
                ivjbillListPanel = new nc.ui.pub.bill.BillListPanel(false);
                ivjbillListPanel.setName("billListPanel");
                // user code begin {1}
                //�����ʾλ��ֵ
                //װ��ģ��
                nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(getBillType(), 
                        getBusinessType(),getOperator(), getPkCorp(), getNodeKey());

                nc.ui.pub.bill.BillListData billDataVo = new nc.ui.pub.bill.BillListData(vo);

                //�����������ʾλ��
                String[][] tmpAry = getHeadShowNum();
                if (tmpAry != null) {
                    setVoDecimalDigitsHead(billDataVo, tmpAry);
                }
                //�����ӱ����ʾλ��
                tmpAry = getBodyShowNum();
                if (tmpAry != null) {
                    setVoDecimalDigitsBody(billDataVo, tmpAry);
                }

                ivjbillListPanel.setListData(billDataVo);

                //�������������е��ж�
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
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                ivjExc.printStackTrace();
                // user code end
            }
        }
        return ivjbillListPanel;
    }
      
    /**
     * ����:  ���õ���״̬������
     * @param billItem
     * @param values
     * @param isWhithIndex
     * @return:void
     * @author ������
     * 2007-10-22 ����02:44:53
     */
    protected void initComboBox(BillItem billItem,Object[] values,boolean isWhithIndex){

        if(billItem != null && billItem.getDataType() == BillItem.COMBO){
            billItem.setWithIndex(isWhithIndex);

            nc.ui.pub.beans.UIComboBox cmb = (nc.ui.pub.beans.UIComboBox) billItem.getComponent();

            cmb.removeAllItems();

            for (int i = 0; i < values.length; i++) {
                  cmb.addItem(values[i]);
            }
        }
    }

    public static boolean isEmpty(Object value) {
        if (value == null){
            return true;
        }
        if ((value instanceof String) && (((String) value).trim().length() <= 0)){
            return true;
        }
        if ((value instanceof Object[]) && (((Object[]) value).length <= 0)) {
            return true;
        }
        //�ж������е�ֵ�Ƿ�ȫ��Ϊ��null.
        if (value instanceof Object[]) {
            Object[] t = (Object[]) value;
            for (int i = 0; i < t.length; i++) {
                if (t[i] != null) {
                    return false;
                }
            }
            return true;
        }
        if ((value instanceof Collection) && ((Collection) value).size() <= 0){
            return true;
        }
        if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0){
            return true;
        }
        if ((value instanceof Map) && ((Map) value).size() <= 0){
            return true;
        }
        return false;
    }


}
