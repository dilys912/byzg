package nc.ui.ic.pub.pf;

import java.awt.Container;
import java.awt.Frame;
import java.util.ArrayList;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.IinitQueryData;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ICBillQuery2YFFP extends QueryConditionDlgForBill
  implements IinitQueryData
{
  public ICBillQuery2YFFP()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      String userid = ce.getUser().getPrimaryKey();
      String date = ce.getDate().toString();
      String pk_corp = ce.getCorporation().getPk_corp();
      initData(pk_corp, userid, "40089906", null, "4E", "4Y", null);
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public ICBillQuery2YFFP(Container parent)
  {
    super(parent);
    try {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      String userid = ce.getUser().getPrimaryKey();
      String date = ce.getDate().toString();
      String pk_corp = ce.getCorporation().getPk_corp();
      initData(pk_corp, userid, "40089906", null, "4E", "4Y", null);
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public ICBillQuery2YFFP(Container parent, String title)
  {
    super(parent, title);
    try {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      String userid = ce.getUser().getPrimaryKey();
      String date = ce.getDate().toString();
      String pk_corp = ce.getCorporation().getPk_corp();
      initData(pk_corp, userid, "40089906", null, "4E", "4Y", null);
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public ICBillQuery2YFFP(Frame parent)
  {
    super(parent);
    try {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      String userid = ce.getUser().getPrimaryKey();
      String date = ce.getDate().toString();
      String pk_corp = ce.getCorporation().getPk_corp();
      initData(pk_corp, userid, "40089906", null, "4E", "4Y", null);
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public ICBillQuery2YFFP(Frame parent, String title)
  {
    super(parent, title);
    try {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      String userid = ce.getUser().getPrimaryKey();
      String date = ce.getDate().toString();
      String pk_corp = ce.getCorporation().getPk_corp();
      initData(pk_corp, userid, "40089906", null, "4E", "4Y", null);
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public String getNodeCode(String strCurrentBillType, String strSourceBillType)
  {
    String nodecode = null;

    if (strSourceBillType.equals(BillTypeConst.m_allocationOut))
      nodecode = "40089906";
    return nodecode;
  }

  public void initData(String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, Object userObj)
    throws Exception
  {
    setTempletID(pkCorp, nc.vo.ic.pub.GenMethod.getNodeCodeByBillType(sourceBilltype), operator, businessType, getNodeCode(currentBillType, sourceBilltype));

    setDefaultValue("head.dbilldate", null, (ClientEnvironment.getInstance().getDate() == null) ? null : ClientEnvironment.getInstance().getDate().toString());

    setDefaultValue("dbilldate", null, (ClientEnvironment.getInstance().getDate() == null) ? null : ClientEnvironment.getInstance().getDate().toString());

    setCombox("body.cfirsttype", new String[][] { { "", "" }, { "5C", NCLangRes.getInstance().getStrByID("4008other", "UPT40080618-000006") }, { "5D", NCLangRes.getInstance().getStrByID("4008other", "UPT40080618-000007") }, { "5E", NCLangRes.getInstance().getStrByID("4008other", "UPT40080618-000008") } });

    setRefInitWhereClause("head.coutcalbodyid", "库存组织", "pk_corp=", "head.coutcorpid");

    setRefInitWhereClause("head.pk_calbody", "库存组织", "pk_corp=", "head.pk_corp");

    setRefInitWhereClause("head.cothercalbodyid", "库存组织", "pk_corp=", "head.cothercorpid");

    setRefInitWhereClause("head.cotherwhid", "仓库档案", "pk_corp=", "head.cothercorpid");

    ArrayList alcorp = new ArrayList();
    alcorp.add(pkCorp);

    initCorpRef("head.cothercorpid", pkCorp, alcorp);

    setCorpRefs("head.cothercorpid", new String[] { "head.cothercalbodyid", "head.cotherwhid", "body.creceieveid" });

    nc.ui.ic.pub.tools.GenMethod.setDataPowerFlag(this, false, new String[] { "head.cothercalbodyid", "head.cotherwhid", "body.creceieveid", "head.coutcalbodyid" });
    nc.ui.ic.pub.tools.GenMethod.setDataPowerFlagByRefName(this, false, new String[] { "存货档案", "存货分类" });
    hideNormal();
    
    QueryConditionVO[] vos = getConditionDatas();
    ArrayList<QueryConditionVO> ca = new ArrayList<QueryConditionVO>();
    for(nc.vo.pub.query.QueryConditionVO vo : vos){
  	 if(! vo.getFieldCode().equals("qbillstatus"))
  		ca.add(vo);
    }
    setConditionDatas(ca.toArray(new QueryConditionVO[ca.size()]));
  
  }
}