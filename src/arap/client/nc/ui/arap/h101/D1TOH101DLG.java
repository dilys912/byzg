package nc.ui.arap.h101;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.impl.arap.proxy.Proxy;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.arap.h101.AbstractBTOBBillQueryDLG;
import nc.vo.arap.h101.AbstractBTOBDLG;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;



/**
 * ˵��:�����ε��ݽ���
 * @author Maoyidong
 * 2007-9-29 ����04:02:28
 */
public class D1TOH101DLG extends AbstractBTOBDLG {
    public D1TOH101DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
       /* 
        BillItem billItemin = getbillListPanel().getBillListData().getBodyItem("Vintcycle");
        initComboBox(billItemin, ISHSHConst.INTCYCLE,true);
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, ISHSHBillStatus.strStateRemark,true);
        BillItem billItempe = getbillListPanel().getBillListData().getHeadItem("pricingtype");
        initComboBox(billItempe, ISHSHConst.PRICINGTYPE,true);
        BillItem billItemst = getbillListPanel().getBillListData().getHeadItem("saletype");
        initComboBox(billItemst, ISHSHConst.SALETYPE,true);
        BillItem billItemmet = getbillListPanel().getBillListData().getHeadItem("metalmny");
        initComboBox(billItemmet, ISHSHConst.METALMNY,true);
        BillItem billItemlight = getbillListPanel().getBillListData().getBodyItem("blight");
        initComboBox(billItemlight, ISHSHConst.BLIGHT,true);
        BillItem billIteminBU = getbillListPanel().getBillListData().getBodyItem("ulight");
        initComboBox(billIteminBU, ISHSHConst.ULIGHT,true);
*/
    }
    IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(
  		  IUAPQueryBS.class.getName());
    
    public void loadHeadData() {
        try {
            // ���ò�Ʒ�鴫��������뵱ǰ��ѯ��������������������ѯ����
            String tmpWhere = null;
            if (!isEmpty(getHeadCondition())) {
                if (m_whereStr == null) {
//                    tmpWhere = " (" + getHeadCondition() + ") order by dmakedate desc";
                    tmpWhere = " (" + getHeadCondition() + ") ";
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
            DJZBHeaderVO[] queryresult = null;
      	  StringBuffer sql = new StringBuffer();
      		
      		sql.append(" select distinct h.* ") 
      		.append(" from arap_djzb h left join arap_djfb b on b.vouchid = h.vouchid and nvl(b.dr,0)=0 ") 
      		.append(" where "+tmpWhere+" and nvl(h.djzt,0)=2 and nvl(b.ybye,0) > 0 and (h.dwbm = '1078' or h.dwbm = '1108') and nvl(h.dr,0)=0 and h.djdl = 'yf'  ");
      		
      		List list = (List) sessionManager.executeQuery(sql.toString(),
      					new BeanListProcessor(DJZBHeaderVO.class));
      		queryresult = new DJZBHeaderVO[list.size()];
      		for (int i = 0; i < list.size(); i++) {
      			queryresult[i]=(DJZBHeaderVO)list.get(i);
      		}
      		DJZBHeaderVO[] parentvo = queryresult;
//            DJZBHeaderVO[] parentvo = Proxy.getIArapBillPublic().queryHead("where " + tmpWhere);
            //�Ѳ�ѯ����VO����Ž�����VO m_Vos��
            if(!isEmpty(parentvo)){
                m_Vos= new AggregatedValueObject[parentvo.length];
                for (int i = 0; i < m_Vos.length; i++) {
                    m_Vos[i]=new DJZBVO();
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

    public String[] getBillVos(){
        return new String[] { DJZBVO.class.getName(),
              DJZBHeaderVO.class.getName(),
              DJZBItemVO.class.getName() };
    	
    }
    
    protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"D1","Ӧ����","D1TOH101");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
//        strWherePart.append(" xslxbm ='"+getBusinessType()+"' and");
//        strWherePart.append("' and vbilltype='"+getBillType()+"'");
        strWherePart.append(" h.dwbm = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and h.djdl = 'yf' and nvl(h.dr,0)=0 and nvl(h.zyx9,0) <> '1'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * ����: ���ر�������
     * @param row
     * @author hk
     */
    public void loadBodyData(int row) {
        try {

            if (m_Vos == null || m_Vos.length < row + 1) {
                System.out.println("����BUG����ͷ��ǰѡ���г�����ͷ����!");
                return;
            }
            // ��ѯ�������ݲ��ٲ�ѯ
            CircularlyAccessibleValueObject[] tmpBodyVo = null;
            if (m_Vos[row].getChildrenVO() == null || m_Vos[row].getChildrenVO().length == 0) {
                String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField())
                        .toString();
                if (id == null) {
                    System.out.println("����BUG����ͷID��TS������ȷ��ȡ!");
                    return;
                }
                tmpBodyVo = (CircularlyAccessibleValueObject[]) m_Vos[row].getChildrenVO();
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
//            SuperVO[] childvo = (SuperVO[]) HYPubBO_Client.queryByCondition(Class.forName(BillVos), tmpWhere);
            DJZBVO zbvo =  Proxy.getIArapBillPublic().findArapBillByPK(pk);
            CircularlyAccessibleValueObject[] childvo = zbvo.getChildrenVO();
            getbillListPanel().setBodyValueVO(childvo);
            getbillListPanel().getBodyBillModel().execLoadFormula();

        } catch (Exception e) {
            e.printStackTrace(System.out);
            if (e instanceof java.rmi.RemoteException)
                nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "���ر���", e.getMessage());
        }
    } 
    
    @Override
    public String getBodyCondition() {
    	StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" nvl(zyx9,0) <> '1'");
        String strwp = strWherePart.toString();
        return strwp;
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
            DJZBItemVO[] child=new DJZBItemVO[list.size()] ;
            child = (DJZBItemVO[])list.toArray(child);
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
    
    
}