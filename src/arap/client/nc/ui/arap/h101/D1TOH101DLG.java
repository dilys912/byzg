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
 * 说明:上下游单据界面
 * @author Maoyidong
 * 2007-9-29 下午04:02:28
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
            // 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
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
            //回去表头VO
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
            //把查询出的VO数组放进缓存VO m_Vos中
            if(!isEmpty(parentvo)){
                m_Vos= new AggregatedValueObject[parentvo.length];
                for (int i = 0; i < m_Vos.length; i++) {
                    m_Vos[i]=new DJZBVO();
                    m_Vos[i].setParentVO(parentvo[i]);
                }

                getbillListPanel().setHeaderValueVO(parentvo);
                getbillListPanel().getHeadBillModel().execLoadFormula(); // 验证公式
                getbillListPanel().getHeadTable().clearSelection();
            }
        } catch (Exception e) {
            System.out.println("数据加载失败！");
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
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"D1","应付单","D1TOH101");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
//        strWherePart.append(" xslxbm ='"+getBusinessType()+"' and");
//        strWherePart.append("' and vbilltype='"+getBillType()+"'");
        strWherePart.append(" h.dwbm = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and h.djdl = 'yf' and nvl(h.dr,0)=0 and nvl(h.zyx9,0) <> '1'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * 功能: 加载表体数据
     * @param row
     * @author hk
     */
    public void loadBodyData(int row) {
        try {

            if (m_Vos == null || m_Vos.length < row + 1) {
                System.out.println("程序BUG：表头当前选中行超出表头行数!");
                return;
            }
            // 查询过的数据不再查询
            CircularlyAccessibleValueObject[] tmpBodyVo = null;
            if (m_Vos[row].getChildrenVO() == null || m_Vos[row].getChildrenVO().length == 0) {
                String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField())
                        .toString();
                if (id == null) {
                    System.out.println("程序BUG：表头ID或TS不能正确获取!");
                    return;
                }
                tmpBodyVo = (CircularlyAccessibleValueObject[]) m_Vos[row].getChildrenVO();
                m_Vos[row].setChildrenVO(tmpBodyVo);
            }
            //获取表体相应行的主键
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
            //回去参照单据中表体VO
            String BillVos = getBillVos()[2];
            //返回根据条件查询的表体
//            SuperVO[] childvo = (SuperVO[]) HYPubBO_Client.queryByCondition(Class.forName(BillVos), tmpWhere);
            DJZBVO zbvo =  Proxy.getIArapBillPublic().findArapBillByPK(pk);
            CircularlyAccessibleValueObject[] childvo = zbvo.getChildrenVO();
            getbillListPanel().setBodyValueVO(childvo);
            getbillListPanel().getBodyBillModel().execLoadFormula();

        } catch (Exception e) {
            e.printStackTrace(System.out);
            if (e instanceof java.rmi.RemoteException)
                nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "加载表体", e.getMessage());
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
     * 功能: 确定按钮，点击时判断是否选中行 
     *      当所选行为多选时，把所有子表VO放在一个数组里返回
     *      返回 retBillVo
     * @author 王建超
     * 2007-10-22 下午03:14:05
     */
    public void onOk() {
       
        //当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
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
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
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
     * 功能: 调用单选时方法getRetVo()
     * @return AggregatedValueObject[]
     * @author 王建超
     * 2007-10-24 下午03:18:48
     */
    public AggregatedValueObject[] getRetVos() {
        
        AggregatedValueObject[] agg = {getRetVo()};
        return agg;
    }
    
    
}