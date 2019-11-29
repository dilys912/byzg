package nc.ui.ic.pub.pf;

import java.awt.Container;
import java.util.ArrayList;

import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.IinitQueryData; 
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;

public class ICBillQueryYFFP extends ICMultiCorpQryClient
  implements IinitQueryData
{
  public ICBillQueryYFFP()
  {
  }

  public ICBillQueryYFFP(Container parent)
  {
    super(parent);
  }

  public String getNodeCode(String strCurrentBillType, String strSourceBillType)
  {
    String nodecode = null;

 
    if (strSourceBillType.equals(BillTypeConst.m_saleOut))
      nodecode = "40089903";
 
    return nodecode;
  }

  public void initData(String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, Object userObj)
    throws Exception
  {
    String srcnodecode = nc.vo.ic.pub.GenMethod.getNodeCodeByBillType(sourceBilltype);
    
    setTempletID(pkCorp, srcnodecode, operator, businessType, getNodeCode(currentBillType, sourceBilltype));

    setCurUserID(operator);
    setLoginCorp(pkCorp);
    setCurFunCode(funNode);

    setPowerCorp(false);
    initCorp();

    setDefaultValue("head.dbilldate", null, (ClientEnvironment.getInstance().getDate() == null) ? null : ClientEnvironment.getInstance().getDate().toString());

    setDefaultValue("dbilldate", null, (ClientEnvironment.getInstance().getDate() == null) ? null : ClientEnvironment.getInstance().getDate().toString());

    hideNormal();

    if (!(getNodeCode(currentBillType, sourceBilltype).equals("40089901"))) {
      hideCorp();
      setPowerRefsOfCorp("pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlgNotByProp(this), null);
    }
    else {
      setPowerRefsOfCorp("pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlgNotByProp(this), null);
    }

    if ((currentBillType != null) && (currentBillType.trim().startsWith("4")) && (!("4453".equals(currentBillType))))
      nc.ui.ic.pub.tools.GenMethod.setDataPowerFlagByRefName(this, false, new String[] { "存货档案", "存货分类" });
    
//    ConditionVO[] vos = getConditionVO();
   QueryConditionVO[] vos = getConditionDatas();
    ArrayList<QueryConditionVO> ca = new ArrayList<QueryConditionVO>();
    for(nc.vo.pub.query.QueryConditionVO vo : vos){
  	 if(! vo.getFieldCode().equals("qbillstatus"))
  		ca.add(vo);
    }
    setConditionDatas(ca.toArray(new QueryConditionVO[ca.size()]));
  }
  
 
}