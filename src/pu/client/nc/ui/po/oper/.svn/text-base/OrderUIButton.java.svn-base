package nc.ui.po.oper;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.vo.pub.BusinessException;

public class OrderUIButton extends PoToftPanelButton
{
  protected ButtonObject[] btnaMsgCenterForAudit;
  public ButtonObject btnMsgCenterAudit;
  public ButtonObject btnMsgCenterUnAudit;
  private ButtonTree m_btnTree;
  //ljy  2014-09-22  
  private ButtonObject btn_BillExcel;

  private ButtonObject btn_OutExcel;
  													

  //zy   2019-07-25
  private ButtonObject btn_Advance;
  



  public OrderUIButton(ToftPanel uiPanel)
  {
    this.btnaMsgCenterForAudit = null;
    this.btnMsgCenterAudit = null;
    this.btnMsgCenterUnAudit = null;
    this.m_btnTree = null;
    this.btn_BillExcel = null;

    this.btn_OutExcel = null;

    this.btn_Advance = null;



    createBtnInstances(uiPanel);
  }

  public ButtonTree getBtnTree(ToftPanel uiPanel)
  {
    if (this.m_btnTree == null)
      try
      {
        this.m_btnTree = new ButtonTree("4004020201");
      }
      catch (BusinessException be)
      {
        uiPanel.showHintMessage(be.getMessage());
        return null;
      }
    return this.m_btnTree;
  }

  public void createBtnInstances(ToftPanel uiPanel)
  {
    super.createBtnInstances(uiPanel);
    this.btnMsgCenterAudit = this.btnBillExecAudit;
    this.btnMsgCenterUnAudit = this.btnBillExecUnAudit;
    //ljy  2014-09-22  
    this.btn_BillExcel = this.btnBillExcel;

 

    //zy   2019-07-25
    this.btn_Advance = this.btnAdvance;


  }

  public ButtonObject[] getBtnaMsgCenter()
  {
    if (this.btnaMsgCenterForAudit == null)
    {
      this.btnBillAssistMsgCenter.addChildButton(this.btnBillStatusQry);
      this.btnBillAssistMsgCenter.addChildButton(this.btnBillDocManage);
      this.btnBillAssistMsgCenter.addChildButton(this.btnBillLnkQry);
      this.btnBillAssistMsgCenter.addChildButton(this.btnBillContractClass);
      this.btnaMsgCenterForAudit = new ButtonObject[] { 

        this.btnMsgCenterAudit, this.btnMsgCenterUnAudit, this.btnBillAssistMsgCenter, this.btn_BillExcel,this.btnBillSave ,this.btn_OutExcel};    //ljy  2014-09-22  

      

    }

    return this.btnaMsgCenterForAudit;
  }
}