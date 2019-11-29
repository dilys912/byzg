package nc.ui.ia.ia302;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.RemoteCall;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.print.PrintDirectEntry;
import nc.vo.ia.ia305.P302;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;

/**
 * �ɱ���̯-�ο�����Ʒ���ɱ�����ڵ㿪��
 * 
 * @author zwx
 * 
 * 2019-10-29
 *
 */
public class PriceallocClientModUI extends ToftPanel
{
	  private static final long serialVersionUID = 7093365038150668151L;
	  private double m_dMaxValue = new UFDouble(1000000000000.0D).doubleValue();

	  private String m_sMessage = "";

	  private ButtonObject m_btnAlloc = new ButtonObject(NCLangRes.getInstance().getStrByID("20146020", "UPT20146020-000001"), NCLangRes.getInstance().getStrByID("20146020", "UPT20146020-000001"), 2, "����");

	  private ButtonObject m_btnCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "��ѯ");

	  private ButtonObject m_btnPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000027"), 2, "��ӡ");

	  private int[] m_iaIncCl = null;

	  private int[] m_iaPrec = null;

	  private PriceAllocQueryPanel m_pnlcondition = null;

	  private PnlInv m_pnlTblInv = null;

	  private PnlInvCl m_pnlTblInvCl = null;

	  private String[] m_sa1 = new String[10];

	  private String[] m_saCond = null;

	  private UserVO m_voUser = null;

	  private IAEnvironment mce = null;

	  private P302 mp = null;

	  private Vector mvDisp = null;

	  private Vector mvQry = null;

	  private Vector mvUpdate = null;

	  private ButtonObject[] m_aryButtonGroup = { this.m_btnCancel, this.m_btnAlloc, this.m_btnPrint };

	  private String m_sCorp = null;

	  private String m_sCorpName = null;

	  private UFDate m_dLogindata = null;

	  private String m_sAccountMonth = null;

	  private String m_sAccountYear = null;

	  private UFDate m_dMonthBeginDate = null;

	  private UFDate m_dMonthEndDate = null;

	  public PriceallocClientModUI()
	  {
	    initialize();
	  }

	  public String getTitle()
	  {
	    return NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000008");
	  }

	  private void handleException(Throwable exception)
	  {
	    Log.error(exception);
	  }

	  private void initialize()
	  {
	    try
	    {
	      setName("PriceallocClientUI");

	      /*SpringLayout layout = new SpringLayout();
	      setLayout(layout);

	      setSize(774, 419);

	      SpringLayout.Constraints constraint = new SpringLayout.Constraints();
	      constraint.setConstraint("West", Spring.constant(0));
	      constraint.setConstraint("North", Spring.constant(0));
	      add(getTableInv(), constraint);

	      constraint = new SpringLayout.Constraints();
	      constraint.setConstraint("West", Spring.constant(0));
	      constraint.setConstraint("North", Spring.constant(0));
	      add(getTableInvCl(), constraint);*/
	      //edit by zwx 2019-10-28 ���ӱ�ͷ������Ϣ
	      BorderLayout card=new BorderLayout();
	      setLayout(card);
	      add(getHeaderPanel(), BorderLayout.NORTH); 
	      add(getTableInv(), BorderLayout.CENTER); 
	      //end by zwx 
	    }
	    catch (Throwable ivjExc) {
	      handleException(ivjExc);
	    }

	    this.m_btnCancel.setEnabled(true);
	    this.m_btnAlloc.setEnabled(false);
	    this.m_btnPrint.setEnabled(false);
	    setButtons(this.m_aryButtonGroup);

	    getTableInvCl().setVisible(false);

	    this.mce = new IAEnvironment();
	    this.m_sCorp = this.mce.getCorporationID();
	    this.m_sCorpName = this.mce.getCorporationName();
	    this.m_voUser = this.mce.getUser();
	    this.m_dLogindata = this.mce.getBusinessDate();
	    this.m_sAccountMonth = this.mce.getAccountMonth();
	    this.m_sAccountYear = this.mce.getAccountYear();
	    try {
	      this.m_dMonthBeginDate = CommonDataBO_Client.getMonthBeginDate(this.m_sCorp, this.m_sAccountYear + "-" + this.m_sAccountMonth);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000009"));

	      return;
	    }
	    try {
	      this.m_dMonthEndDate = CommonDataBO_Client.getMonthEndDate(this.m_sCorp, this.m_sAccountYear + "-" + this.m_sAccountMonth);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000010"));

	      return;
	    }
	    try {
	      this.m_iaIncCl = CommonDataBO_Client.getCodeSchemdule("0001");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000011"));

	      return;
	    }
	    try {
	      this.m_iaPrec = this.mce.getDataPrecision(this.m_sCorp);
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000012"));

	      return;
	    }
	    getTableInv().setPrec(this.m_iaPrec);
	    getTableInvCl().setPrec(this.m_iaPrec);
//	    setLayout(new CardLayout());
	  }

	  public void onAllocButtonClicked()
	  {
	    this.m_sMessage = "";

	    if (this.m_saCond[12].equals("T")) {
	      if (getTableInvCl().iserror()) {
	        return;
	      }
	      this.mvUpdate = getTableInvCl().getdata();
	    }
	    else {
	      if (getTableInv().iserror()) {
	        return;
	      }
	      this.mvUpdate = getTableInv().getdata();
	    }
	    getTableInvCl().removeTotal(this.mvUpdate);

	    this.mvUpdate = checkdata(this.mvUpdate);
	    if ((this.mvUpdate != null) && (this.mvUpdate.size() > 0))
	    {
	      try {
	    	//add by zwx 2019-10-29 ���ӱ����з�̯�ܽ�����ͷ�Ļ��ܽ��һ��
	    	 int iCount = mvUpdate.size();
	    	 UFDouble sumMny = new UFDouble();
	         for(int i = 0; i < iCount; i++){
	        	 Vector  v1 = (Vector)mvUpdate.elementAt(i);
	        	 UFDouble dMny = new UFDouble();
	        	 if(this.m_saCond[12].equals("T")){//�жϴ������/����
	        		 dMny= new UFDouble(v1.elementAt(3).toString().trim()); 
	        	 }else{
	        		 dMny= new UFDouble(v1.elementAt(6).toString().trim()); 
	        	 }
	            sumMny = sumMny.add(dMny);
	         }
	         String rs = checkSumMny(sumMny);
	         if(rs.length()>0){
	        	 showWarningMessage(rs);
	        	 return;
	         }
	    	//end by zwx
	        ObjectUtils.objectReference(this.mvUpdate);

	        P302 p = new P302();
	        p.setSysInfo(this.m_sa1);
	        p.setCondition(this.m_saCond);
	        p.setVec(this.mvUpdate);
	        p.setUser(this.m_voUser.getPrimaryKey());
	        p.setPrec(this.m_iaPrec);
	        p.setIncCl(this.m_iaIncCl);
	        p.setCount(this.mp.getCount());
	        PriceAllocBO_Client.update(p);
	      }
	      catch (Exception ex) {
	        ExceptionUtils.wrappException(ex);
	      }

	      if (this.m_saCond[10].equals("T"))
	      {
	        if (!this.m_saCond[12].equals("T"));
	      }
	      else if (this.m_saCond[12].equals("T")) {
	        getTableInvCl().setEdit(false);
	      }
	      else {
	        getTableInv().setEdit(false);
	      }

	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000014"));
	    }
	    else if (this.m_sMessage.length() != 0) {
	      ExceptionUtils.wrappBusinessException(this.m_sMessage);
	    }
	    else
	    {
	      String message = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000015");

	      ExceptionUtils.wrappBusinessException(message);
	    }
	  }

	  public void onButtonClicked(ButtonObject bo)
	  {
	    RemoteCall call = new RemoteCall();
	    try {
	      if (bo == this.m_btnCancel) {
	    	//add by zwx 2019-10-29 ��ѯǰ��ձ�ͷ��ʷ��д��¼
	    	 cleanText();
	    	//end by zwx
	        onCancleButtonClicked();
	      }
	      else if (bo == this.m_btnAlloc) {
	        call.execute(this, this, "onAllocButtonClicked");
	      }
	      else if (bo == this.m_btnPrint)
	        onPrintButtonClicked();
	    }
	    catch (RuntimeException ex)
	    {
	      ExceptionUITools.showMessage(ex, this);
	    }
	  }

	  private void onCancleButtonClicked()
	  {
	    if (this.m_pnlcondition == null) {
	      this.m_pnlcondition = new PriceAllocQueryPanel(this);

	      this.m_pnlcondition.showModal();
	    }
	    else {
	      this.m_pnlcondition.setVisible(true);
	    }

	    if (this.m_pnlcondition.isCloseOK())
	    {
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000016"));
	      try
	      {
	        this.m_saCond = this.m_pnlcondition.getConditions();
	      }
	      catch (Exception ex)
	      {
	        ExceptionUtils.wrappException(ex);
	      }

	      this.m_sa1 = new String[10];
	      this.m_sa1[0] = this.m_sCorp;
	      this.m_sa1[1] = this.m_dLogindata.toString();
	      this.m_sa1[2] = this.m_sAccountYear;
	      this.m_sa1[3] = this.m_sAccountMonth;
	      this.m_sa1[4] = this.m_dMonthBeginDate.toString();
	      this.m_sa1[5] = this.m_dMonthEndDate.toString();
	      this.m_sa1[6] = this.m_voUser.getPrimaryKey();
	      try {
	        this.mp = new P302();
	        this.mp.setSysInfo(this.m_sa1);
	        this.mp.setCondition(this.m_saCond);
	        this.mp.setPrec(this.m_iaPrec);
	        this.mp.setIncCl(this.m_iaIncCl);
	        this.mp = PriceAllocBO_Client.getdata(this.mp);
	        this.mvQry = this.mp.getVec();
	      }
	      catch (Exception ex) {
	        ExceptionUtils.wrappException(ex);
	      }
	      if ((this.mvQry != null) && (this.mvQry.size() != 0))
	      {
	        this.m_btnCancel.setEnabled(true);
	        this.m_btnAlloc.setEnabled(true);
	        this.m_btnPrint.setEnabled(true);
	        setButtons(this.m_aryButtonGroup);

	        this.mvDisp = null;
	        this.mvDisp = this.mvQry;

	        if (this.m_saCond[12].equals("T")) {
	          getTableInvCl().display(this.mvDisp, this.m_saCond[12]);
	          getTableInvCl().setVisible(true);
	          getTableInv().setVisible(false);
	          getTableInvCl().setEdit(true);
	          add(getTableInvCl(), BorderLayout.CENTER);//add by zwx 2019-10-29
	        }
	        else {
	          getTableInv().display(this.mvDisp, this.m_saCond[12]);
	          getTableInv().setVisible(true);
	          getTableInvCl().setVisible(false);
	          getTableInv().setEdit(true);
	          add(getTableInv(), BorderLayout.CENTER); //add by zwx 2019-10-29
	        }

	        String[] args = new String[1];
	        args[0] = String.valueOf(this.mvDisp.size());
	        String message = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000028", null, args);

	        showHintMessage(message);
	      }
	      else {
	        getTableInv().display(null, this.m_saCond[12]);
	        getTableInvCl().display(null, this.m_saCond[12]);
	        showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000017"));
	      }
	    }
	  }

	  public void onOkButtonClicked()
	  {
	    showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000016"));
	    try
	    {
	      this.m_saCond = this.m_pnlcondition.getConditions();
	    }
	    catch (Exception e1) {
	      e1.printStackTrace();
	    }
	    this.m_sa1 = new String[10];
	    this.m_sa1[0] = this.m_sCorp;
	    this.m_sa1[1] = this.m_dLogindata.toString();
	    this.m_sa1[2] = this.m_sAccountYear;
	    this.m_sa1[3] = this.m_sAccountMonth;
	    this.m_sa1[4] = this.m_dMonthBeginDate.toString();
	    this.m_sa1[5] = this.m_dMonthEndDate.toString();
	    this.m_sa1[6] = this.m_voUser.getPrimaryKey();
	    try {
	      this.mp = new P302();
	      this.mp.setSysInfo(this.m_sa1);
	      this.mp.setCondition(this.m_saCond);
	      this.mp.setPrec(this.m_iaPrec);
	      this.mp.setIncCl(this.m_iaIncCl);
	      this.mp = PriceAllocBO_Client.getdata(this.mp);
	      this.mvQry = this.mp.getVec();
	    }
	    catch (BusinessException e) {
	      e.printStackTrace();
	      showHintMessage(e.getMessage());
	      return;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000013"));

	      return;
	    }
	    if ((this.mvQry != null) && (this.mvQry.size() != 0))
	    {
	      this.m_btnCancel.setEnabled(true);
	      this.m_btnAlloc.setEnabled(true);
	      this.m_btnPrint.setEnabled(true);
	      setButtons(this.m_aryButtonGroup);

	      this.mvDisp = null;
	      this.mvDisp = this.mvQry;

	      if (this.m_saCond[12].equals("T")) {
	        getTableInvCl().display(this.mvDisp, this.m_saCond[12]);
	        getTableInvCl().setVisible(true);
	        getTableInvCl().setEdit(true);
	      }
	      else {
	        getTableInv().display(this.mvDisp, this.m_saCond[12]);
	        getTableInv().setVisible(true);
	        getTableInv().setEdit(true);
	      }

	      String[] args = new String[1];
	      args[0] = String.valueOf(this.mvDisp.size());
	      String message = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000028", null, args);

	      showHintMessage(message);
	    }
	    else {
	      showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000017"));
	    }
	  }

	  private void onPrintButtonClicked()
	  {
	    String[][] sa2ColName = (String[][])null;
	    Vector v = null;
	    Object[][] oa2p = (Object[][])null;
	    int[] iaColWidth = null;
	    int[] iaFlag = null;
	    if (this.m_saCond[12].equals("T")) {
	      sa2ColName = new String[][] { { NCLangRes.getInstance().getStrByID("common", "UC000-0001449"), NCLangRes.getInstance().getStrByID("common", "UC000-0001446"), NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"), NCLangRes.getInstance().getStrByID("common", "UC000-0001945"), NCLangRes.getInstance().getStrByID("common", "UC000-0001775") } };

	      v = getTableInvCl().getdata();

	      oa2p = new Object[v.size()][5];
	      int iCount = v.size();
	      for (int i = 0; i < iCount; i++) {
	        for (int j = 0; j < 5; j++) {
	          oa2p[i][j] = ((Vector)v.elementAt(i)).elementAt(j);
	        }
	      }
	      iaColWidth = new int[] { 100, 100, 100, 100, 100 };

	      iaFlag = new int[] { 0, 0, 2, 2, 2 };
	    }
	    else
	    {
	      sa2ColName = new String[][] { { NCLangRes.getInstance().getStrByID("common", "UC000-0002930"), NCLangRes.getInstance().getStrByID("common", "UC000-0001480"), NCLangRes.getInstance().getStrByID("common", "UC000-0001453"), NCLangRes.getInstance().getStrByID("common", "UC000-0003448"), NCLangRes.getInstance().getStrByID("common", "UC000-0001240"), NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"), NCLangRes.getInstance().getStrByID("common", "UC000-0001945"), NCLangRes.getInstance().getStrByID("common", "UC000-0001775") } };

	      v = getTableInv().getdata();
	      oa2p = new Object[v.size()][8];
	      int iCount = v.size();
	      for (int i = 0; i < iCount; i++) {
	        for (int j = 0; j < 8; j++) {
	          oa2p[i][j] = ((Vector)v.elementAt(i)).elementAt(j);
	        }
	      }
	      iaColWidth = new int[] { 100, 80, 80, 80, 80, 90, 90, 90 };

	      iaFlag = new int[] { 0, 0, 0, 0, 0, 2, 2, 2 }; } 
	String sTitle = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000008");

	    String[] args = new String[3];
	    args[0] = this.m_sCorpName;
	    args[1] = this.m_sAccountYear;
	    args[2] = this.m_sAccountMonth;
	    String sTopstr = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000029", null, args);

	    args = new String[2];
	    args[0] = this.m_voUser.getUserName();
	    args[1] = this.m_dLogindata.toString();
	    String sBotstr = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000030", null, args);

	    PrintDirectEntry print = new PrintDirectEntry();
	    print.setFixedRows(1);
	    print.setTitle(sTitle);
	    print.setTopStr(sTopstr);
	    print.setBottomStr(sBotstr);
	    print.setColWidth(iaColWidth);
	    print.setAlignFlag(iaFlag);
	    print.setColNames(sa2ColName);
	    print.setData(oa2p);
	    print.preview();
	    if (this.m_saCond[12].equals("T")); } 
	  private Vector checkdata(Vector v) { Vector data = new Vector();
	    String sTmp = null;
	    int iCount = v.size();
	    int iPos;
	    if (this.m_saCond[12].equals("T")) {
	      iPos = 4;
	    }
	    else {
	      iPos = 7;
	    }
	    for (int i = 0; i < iCount; i++) {
	      boolean canAlloc = true;
	      if (((Vector)v.elementAt(i)).elementAt(iPos) == null) {
	        String[] args = new String[1];
	        args[0] = String.valueOf(i + 1);

	        canAlloc = false;
	      }
	      sTmp = ((Vector)v.elementAt(i)).elementAt(iPos).toString();
	      String sTmpMny = ((Vector)v.elementAt(i)).elementAt(iPos - 1).toString();
	      if (sTmp.length() == 0) {
	        String[] args = new String[1];
	        args[0] = String.valueOf(i + 1);

	        canAlloc = false;
	      }

	      if (new UFDouble(sTmp).doubleValue() < 0.0D) {
	        String[] args = new String[1];
	        args[0] = String.valueOf(i + 1);
	        this.m_sMessage = NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000025", null, args);

	        canAlloc = false;

	        return null;
	      }
	      if (new UFDouble(sTmpMny).doubleValue() > this.m_dMaxValue)
	      {
	        String[] value = { String.valueOf(this.m_dMaxValue) };

	        this.m_sMessage = NCLangRes.getInstance().getStrByID("20143010", "UPP20143010-000164", null, value);

	        canAlloc = false;

	        return null;
	      }
	      if (canAlloc) {
	        data.add(v.elementAt(i));
	      }
	    }
	    return data;
	  }

	  private PnlInv getTableInv()
	  {
	    if (this.m_pnlTblInv == null) {
	      try {
	        this.m_pnlTblInv = new PnlInv();
	        this.m_pnlTblInv.setName("PnlInv");
	      }
	      catch (Throwable ivjExc) {
	        handleException(ivjExc);
	      }
	    }
	    return this.m_pnlTblInv;
	  }

	  private PnlInvCl getTableInvCl()
	  {
	    if (this.m_pnlTblInvCl == null) {
	      try {
	        this.m_pnlTblInvCl = new PnlInvCl();
	        this.m_pnlTblInvCl.setName("m_pnlTblInvCl");
	      }
	      catch (Throwable ivjExc) {
	        handleException(ivjExc);
	      }
	    }
	    return this.m_pnlTblInvCl;
	  }
	  
	  
	  //add by zwx 2019-10-28 ��ͬ�������
	    JTextField zjcl = null;//ֱ�Ӳ���
		JTextField bzw = null;//��װ��
		JTextField fl = null;//����
		JTextField jwl = null;//������
		JTextField zzfy = null;//�������
		JTextField zjrg = null;//ֱ���˹�
		JTextField sdm = null;//ˮ��ú
		JTextField bj = null;//����
		JTextField feilv = null;//����
		JTextField zczj = null;//�̶��ʲ��۾�
		JTextField jjrg = null;//����˹�
		JTextField bdfy = null;//�䶯�������
		JTextField dq = null;//�硢��
		
		
	  /**
		 * �½��ײ�Panel ��������ʾ������/����/�����
		 * */
		public JPanel getHeaderPanel() {
			JPanel bpanel = new JPanel();

			String unitname = getClientEnvironment().getInstance().getCorporation().getUnitname();
			//���ݰ����ʾ��ͬ����
			if(unitname.contains("�ƹ�")){
				zjcl = new JTextField("",8);//ֱ�Ӳ���
				bzw = new JTextField("",8);//��װ��
				fl = new JTextField("",8);//����
				jwl = new JTextField("",8);//������
				zzfy = new JTextField("",8);//�������
				zjrg = new JTextField("",8);//ֱ���˹�
				sdm = new JTextField("",8);//ˮ��ú
				
				// ���ÿɱ༭
				zjcl.setEditable(true);
				bzw.setEditable(true);
				fl.setEditable(true);
				jwl.setEditable(true);
				zzfy.setEditable(true);
				zjrg.setEditable(true);
				sdm.setEditable(true);
				
				// �������
				bpanel.add(new JLabel("ֱ�Ӳ���:"));
				bpanel.add(zjcl);
				bpanel.add(new JLabel("��װ��:"));
				bpanel.add(bzw);
				bpanel.add(new JLabel("����:"));
				bpanel.add(fl);
				bpanel.add(new JLabel("������:"));
				bpanel.add(jwl);
				bpanel.add(new JLabel("�������:"));
				bpanel.add(zzfy);
				bpanel.add(new JLabel("ֱ���˹�:"));
				bpanel.add(zjrg);
				bpanel.add(new JLabel("ˮ��ú:"));
				bpanel.add(sdm);
				
			}else if(unitname.contains("�Ƹ�")){
				zjcl = new JTextField("",8);//ֱ�Ӳ���
				bj = new JTextField("",8);//����
				fl = new JTextField("",8);//����
				feilv = new JTextField("",8);//����
				zczj = new JTextField("",8);//�̶��ʲ��۾�
				zjrg = new JTextField("",8);//ֱ���˹�
				jjrg = new JTextField("",8);//����˹�
				bdfy = new JTextField("",8);//�䶯�������
				
				// ���ÿɱ༭
				zjcl.setEditable(true);
				bj.setEditable(true);
				fl.setEditable(true);
				feilv.setEditable(true);
				zczj.setEditable(true);
				zjrg.setEditable(true);
				jjrg.setEditable(true);
				bdfy.setEditable(true);
				
				// �������
				bpanel.add(new JLabel("ֱ�Ӳ���:"));
				bpanel.add(zjcl);
				bpanel.add(new JLabel("����:"));
				bpanel.add(bj);
				bpanel.add(new JLabel("����:"));
				bpanel.add(fl);
				bpanel.add(new JLabel("����:"));
				bpanel.add(feilv);
				bpanel.add(new JLabel("�̶��ʲ��۾�:"));
				bpanel.add(zczj);
				bpanel.add(new JLabel("ֱ���˹�:"));
				bpanel.add(zjrg);
				bpanel.add(new JLabel("����˹�:"));
				bpanel.add(jjrg);
				bpanel.add(new JLabel("�䶯�������:"));
				bpanel.add(bdfy);
				
			}else if(unitname.contains("ӡ��")){
				zjcl = new JTextField("",8);//ֱ�Ӳ���
				bzw = new JTextField("",8);//��װ��
				fl = new JTextField("",8);//����
				dq = new JTextField("",8);//�硢��
				zzfy = new JTextField("",8);//�̶��������
				bdfy = new JTextField("",8);//�䶯�������
				zjrg = new JTextField("",8);//ֱ���˹�
				
				// ���ÿɱ༭
				zjcl.setEditable(true);
				bzw.setEditable(true);
				fl.setEditable(true);
				dq.setEditable(true);
				zzfy.setEditable(true);
				bdfy.setEditable(true);
				zjrg.setEditable(true);
				
				// �������
				bpanel.add(new JLabel("ֱ�Ӳ���:"));
				bpanel.add(zjcl);
				bpanel.add(new JLabel("��װ��:"));
				bpanel.add(bzw);
				bpanel.add(new JLabel("����:"));
				bpanel.add(fl);
				bpanel.add(new JLabel("�硢��:"));
				bpanel.add(dq);
				bpanel.add(new JLabel("�̶��������:"));
				bpanel.add(zzfy);
				bpanel.add(new JLabel("�䶯�������:"));
				bpanel.add(bdfy);
				bpanel.add(new JLabel("ֱ���˹�:"));
				bpanel.add(zjrg);
			}
			
			return bpanel;
		}
		
		/**
		 * ���ݱ����еĻ��ܷ�̯������ͷ���ı��������У��
		 * һ������з�̯
		 * ��һ����ʾ����
		 * @param sumMny
		 * @return
		 */
		private String checkSumMny(UFDouble sumMny){
			String unitname = getClientEnvironment().getInstance().getCorporation().getUnitname();
			//���ݰ����ʾ��ͬ����
			UFDouble headSum = new UFDouble();
			if(unitname.contains("�ƹ�")){
				UFDouble zjnum = zjcl.getText()==null?new UFDouble(0.0):new UFDouble(zjcl.getText());
				UFDouble bznum = bzw.getText()==null?new UFDouble(0.0):new UFDouble(bzw.getText());
				UFDouble flnum = fl.getText()==null?new UFDouble(0.0):new UFDouble(fl.getText());
				UFDouble jwlnum = jwl.getText()==null?new UFDouble(0.0):new UFDouble(jwl.getText());
				UFDouble zzfynum = zzfy.getText()==null?new UFDouble(0.0):new UFDouble(zzfy.getText());
				UFDouble zjrgnum = zjrg.getText()==null?new UFDouble(0.0):new UFDouble(zjrg.getText());
				UFDouble sdmnum = sdm.getText()==null?new UFDouble(0.0):new UFDouble(sdm.getText());
				headSum = headSum.add(zjnum).add(bznum).add(flnum).add(jwlnum).add(zzfynum).add(zjrgnum).add(sdmnum);
			}else if(unitname.contains("�Ƹ�")){
				UFDouble zjnum = zjcl.getText()==null?new UFDouble(0.0):new UFDouble(zjcl.getText());
				UFDouble bjnum = bj.getText()==null?new UFDouble(0.0):new UFDouble(bj.getText());
				UFDouble flnum = fl.getText()==null?new UFDouble(0.0):new UFDouble(fl.getText());
				UFDouble feilvnum = feilv.getText()==null?new UFDouble(0.0):new UFDouble(feilv.getText());
				UFDouble zczjnum = zczj.getText()==null?new UFDouble(0.0):new UFDouble(zczj.getText());
				UFDouble zjrgnum = zjrg.getText()==null?new UFDouble(0.0):new UFDouble(zjrg.getText());
				UFDouble jjrgnum = jjrg.getText()==null?new UFDouble(0.0):new UFDouble(jjrg.getText());
				UFDouble bdfynum = bdfy.getText()==null?new UFDouble(0.0):new UFDouble(bdfy.getText());
				headSum = headSum.add(zjnum).add(bjnum).add(flnum).add(feilvnum).add(zczjnum).add(zjrgnum).add(jjrgnum).add(bdfynum);
			}else if(unitname.contains("ӡ��")){
				UFDouble zjnum = zjcl.getText()==null?new UFDouble(0.0):new UFDouble(zjcl.getText());
				UFDouble bznum = bzw.getText()==null?new UFDouble(0.0):new UFDouble(bzw.getText());
				UFDouble flnum = fl.getText()==null?new UFDouble(0.0):new UFDouble(fl.getText());
				UFDouble dqnum = dq.getText()==null?new UFDouble(0.0):new UFDouble(dq.getText());
				UFDouble zzfynum = zzfy.getText()==null?new UFDouble(0.0):new UFDouble(zzfy.getText());
				UFDouble bdfynum = bdfy.getText()==null?new UFDouble(0.0):new UFDouble(bdfy.getText());
				UFDouble zjrgnum = zjrg.getText()==null?new UFDouble(0.0):new UFDouble(zjrg.getText());
				headSum = headSum.add(zjnum).add(bznum).add(flnum).add(dqnum).add(zzfynum).add(bdfynum).add(zjrgnum);
			}else{//������˾������У�飬��ԭ�������̽���
				return "";
			}
			if(headSum.doubleValue()==sumMny.doubleValue()){
				return "";
			}else{
				return "��ͷ���ϼƣ�"+headSum+"���ܳɱ���"+sumMny+"��һ�£����޸�ȷ�ϣ�";
			}
		}
		
		private void cleanText() {
			String unitname = getClientEnvironment().getInstance().getCorporation()
					.getUnitname();
			// ���ݰ����ʾ��ͬ����
			if (unitname.contains("�ƹ�")) {
				zjcl.setText("");// ֱ�Ӳ���
				bzw.setText("");
				fl.setText("");
				jwl.setText("");
				zzfy.setText("");
				zjrg.setText("");
				sdm.setText("");
			} else if (unitname.contains("�Ƹ�")) {
				zjcl.setText("");
				bj.setText("");
				fl.setText("");
				feilv.setText("");
				zczj.setText("");
				zjrg.setText("");
				jjrg.setText("");
				bdfy.setText("");
			} else if (unitname.contains("ӡ��")) {
				zjcl.setText("");
				bzw.setText("");
				fl.setText("");
				dq.setText("");
				zzfy.setText("");
				bdfy.setText("");
				zjrg.setText("");
			}
		}
		//end by zwx 
	  
}