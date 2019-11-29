package nc.ui.ia.ia302;

import java.util.Vector;

import javax.swing.Spring;
import javax.swing.SpringLayout;

import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.RemoteCall;
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
 *
 */
public class PriceallocClientUI extends ToftPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 7093365038150668151L;

  private double m_dMaxValue = new UFDouble(999999999999.99999999)
      .doubleValue();

  private String m_sMessage = "";

  private ButtonObject m_btnAlloc = new ButtonObject(
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPT20146020-000001") /* @res "����" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPT20146020-000001") /* @res "����" */, 2, "����"); /*-=notranslate=-*/

  private ButtonObject m_btnCancel = new ButtonObject(nc.ui.ml.NCLangRes
      .getInstance().getStrByID("common", "UC001-0000006") /* @res "��ѯ" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")
      /* @res "���ز�ѯ����" */, 2, "��ѯ"); /*-=notranslate=-*/

  // ��ť����
  // private ButtonObject m_btnOk = new ButtonObject( nc.ui.ml.NCLangRes.
  // getInstance().getStrByID( "common", "UC001-0000044" ) /*@res "ȷ��"*/,
  // nc.
  // ui.ml.NCLangRes.getInstance().getStrByID( "common",
  // "UC001-0000044" ) /*@res "ȷ��"*/,
  // 2, "ȷ��" ); /*-=notranslate=-*/
  private ButtonObject m_btnPrint = new ButtonObject(nc.ui.ml.NCLangRes
      .getInstance().getStrByID("common", "UC001-0000007") /* @res "��ӡ" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000027")
      /* @res "��ӡ���" */, 2, "��ӡ"); /*-=notranslate=-*/

  private int[] m_iaIncCl = null; // ������༶��

  private int[] m_iaPrec = null;

  private PriceAllocQueryPanel m_pnlcondition = null;

  private PnlInv m_pnlTblInv = null;

  private PnlInvCl m_pnlTblInvCl = null;

  private String[] m_sa1 = new String[10];

  // �������ѡ��
  private String[] m_saCond = null;

  private UserVO m_voUser = null;

  // ϵͳ��Ϣ
  private IAEnvironment mce = null;

  private P302 mp = null;

  private Vector mvDisp = null; // ��ʾ����

  // private String[] m_saBill = null; //��ѯ����еĵ��ݷ�¼ID��Ϣ
  private Vector mvQry = null; // ��ѯ�������

  private Vector mvUpdate = null; // �û����µ�����

  // ��ť���鶨��
  private ButtonObject[] m_aryButtonGroup = {
      this.m_btnCancel, this.m_btnAlloc, this.m_btnPrint
  };

  private String m_sCorp = null;

  private String m_sCorpName = null;

  private UFDate m_dLogindata = null;

  private String m_sAccountMonth = null;

  private String m_sAccountYear = null;

  private UFDate m_dMonthBeginDate = null;

  private UFDate m_dMonthEndDate = null;

  /**
   * PriceallocClientUI ������ע�⡣
   */
  public PriceallocClientUI() {
    super();
    this.initialize();
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000008") /* @res "����Ʒ���ɱ�����" */;
  }

  /**
   * ÿ�������׳��쳣ʱ������
   * 
   * @param exception
   *          java.lang.Throwable
   */
  private void handleException(java.lang.Throwable exception) {
    Log.error(exception);
  }

  /**
   * ��ʼ���ࡣ
   */
  /* ���棺�˷������������ɡ� */
  private void initialize() {
    try {
      // user code begin {1}
      // user code end
      this.setName("PriceallocClientUI");

      SpringLayout layout = new SpringLayout();
      this.setLayout(layout);

      this.setSize(774, 419);

      // SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      // constraint.setConstraint( SpringLayout.WEST, Spring.constant( 0 ) );
      // constraint.setConstraint( SpringLayout.NORTH, Spring.constant( 0 ) );
      // add( getPriceAllocPanel1(), constraint );

      // SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      // constraint.setConstraint( SpringLayout.WEST, Spring.constant( 0 ) );
      // constraint.setConstraint( SpringLayout.NORTH, Spring.constant( 0 ) );
      // add( getPriceAllocQueryPanel(), constraint );

      SpringLayout.Constraints constraint = new SpringLayout.Constraints();
      constraint.setConstraint(SpringLayout.WEST, Spring.constant(0));
      constraint.setConstraint(SpringLayout.NORTH, Spring.constant(0));
      this.add(this.getTableInv(), constraint);

      constraint = new SpringLayout.Constraints();
      constraint.setConstraint(SpringLayout.WEST, Spring.constant(0));
      constraint.setConstraint(SpringLayout.NORTH, Spring.constant(0));
      this.add(this.getTableInvCl(), constraint);
    }
    catch (java.lang.Throwable ivjExc) {
      this.handleException(ivjExc);
    }
    // user code begin {2}
    this.m_btnCancel.setEnabled(true);
    this.m_btnAlloc.setEnabled(false);
    this.m_btnPrint.setEnabled(false);
    this.setButtons(this.m_aryButtonGroup);
    // getPriceAllocQueryPanel().setVisible(false);
    this.getTableInvCl().setVisible(false);
    // ��ȡϵͳ��Ϣ
    this.mce = new IAEnvironment();
    this.m_sCorp = this.mce.getCorporationID();
    this.m_sCorpName = this.mce.getCorporationName();
    this.m_voUser = this.mce.getUser();
    this.m_dLogindata = this.mce.getBusinessDate();
    this.m_sAccountMonth = this.mce.getAccountMonth();
    this.m_sAccountYear = this.mce.getAccountYear();
    try {
      this.m_dMonthBeginDate = CommonDataBO_Client.getMonthBeginDate(this.m_sCorp,
          this.m_sAccountYear + "-" + this.m_sAccountMonth);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000009") /* @res "��ȡ�������ʼ�մ���" */);
      return;
    }
    try {
      this.m_dMonthEndDate = CommonDataBO_Client.getMonthEndDate(this.m_sCorp,
          this.m_sAccountYear + "-" + this.m_sAccountMonth);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000010") /* @res "��ȡ�������ֹ�մ���" */);
      return;
    }
    try {
      this.m_iaIncCl = CommonDataBO_Client.getCodeSchemdule("0001");
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000011") /* @res "��ȡ����������" */);
      return;
    }
    try {
      this.m_iaPrec = this.mce.getDataPrecision(this.m_sCorp);
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000012") /* @res "��ȡ���ݾ��ȴ���" */);
      return;
    }
    this.getTableInv().setPrec(this.m_iaPrec);
    this.getTableInvCl().setPrec(this.m_iaPrec);
    this.setLayout(new java.awt.CardLayout());
    // user code end
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(01-4-25 14:41:34)
   */
  public void onAllocButtonClicked() {
    this.m_sMessage = "";
    // �Ƿ��մ��������гɱ�����
    if (this.m_saCond[12].equals("T")) {
      if (this.getTableInvCl().iserror()) {
        return;
      }
      this.mvUpdate = this.getTableInvCl().getdata();
    }
    else {
      if (this.getTableInv().iserror()) {
        return;
      }
      this.mvUpdate = this.getTableInv().getdata();
    }
    this.getTableInvCl().removeTotal(this.mvUpdate);

    // ��������Ƿ��Ѿ�¼����ȷ
    this.mvUpdate = this.checkdata(this.mvUpdate);
    if ((this.mvUpdate != null) && (this.mvUpdate.size() > 0)) {
      try {
        // ��VO�������úϲ����������紫����
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
      // �����гɱ��Ĳ���Ʒ��ⵥ�Ƿ����·���
      if (this.m_saCond[10].equals("T")) {
        if (this.m_saCond[12].equals("T")) {
        }
        else {
        } // if
      }
      else {
        // �Ƿ��մ��������гɱ�����
        if (this.m_saCond[12].equals("T")) {
          this.getTableInvCl().setEdit(false);
        }
        else {
          this.getTableInv().setEdit(false);
        } // if
      } // if
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000014") /* @res "����ɹ�" */);
    }
    else if (this.m_sMessage.length() != 0) {
      ExceptionUtils.wrappBusinessException(this.m_sMessage);

    }
    else {
      String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000015"); /* @res "����������" */
      ExceptionUtils.wrappBusinessException(message);
    }
  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    RemoteCall call = new RemoteCall();
    try {
      if (bo == this.m_btnCancel) {
        this.onCancleButtonClicked();
      }
      else if (bo == this.m_btnAlloc) {
        call.execute(this, this, "onAllocButtonClicked");
      }
      else if (bo == this.m_btnPrint) {
        this.onPrintButtonClicked();
      }
    }
    catch (RuntimeException ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(01-4-25 9:54:53)
   */
  /*
   * private void onCancleButtonClicked() { getTableInv().setVisible( false );
   * getTableInvCl().setVisible( false ); getPriceAllocPanel1().setVisible( true );
   * //���ð�ť�仯 // m_btnOk.setEnabled( true ); m_btnCancel.setEnabled( false );
   * m_btnAlloc.setEnabled( false ); m_btnPrint.setEnabled( false ); setButtons(
   * m_aryButtonGroup ); showHintMessage( "" ); }
   */
  private void onCancleButtonClicked() {

    if (this.m_pnlcondition == null) {
      this.m_pnlcondition = new PriceAllocQueryPanel(this);

      this.m_pnlcondition.showModal();
    }
    else {
      this.m_pnlcondition.setVisible(true);
    }

    if (this.m_pnlcondition.isCloseOK()) {

      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000016") /* @res "��ȴ�" */);
      // �Ӵ�������������л������
      // m_saCond = getPriceAllocPanel1().getcondition();

      try {
        this.m_saCond = this.m_pnlcondition.getConditions();

      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }

      this.m_sa1 = new String[10];
      this.m_sa1[0] = this.m_sCorp; // ��¼��λ
      this.m_sa1[1] = this.m_dLogindata.toString(); // ��¼����
      this.m_sa1[2] = this.m_sAccountYear; // ��¼�����
      this.m_sa1[3] = this.m_sAccountMonth; // ��¼�����
      this.m_sa1[4] = this.m_dMonthBeginDate.toString(); // ��ǰ����¿�ʱ��
      this.m_sa1[5] = this.m_dMonthEndDate.toString(); // ��ǰ����½�����
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
      if ((this.mvQry != null) && (this.mvQry.size() != 0)) {
        // ���ð�ť�仯
        // m_btnOk.setEnabled( false );
        this.m_btnCancel.setEnabled(true);
        this.m_btnAlloc.setEnabled(true);
        this.m_btnPrint.setEnabled(true);
        this.setButtons(this.m_aryButtonGroup);

        this.mvDisp = null;
        this.mvDisp = this.mvQry;
        // �����Ƿ��մ��������гɱ�������ʾ����
        if (this.m_saCond[12].equals("T")) {
          this.getTableInvCl().display(this.mvDisp, this.m_saCond[12]);
          this.getTableInvCl().setVisible(true);
          this.getTableInv().setVisible(false);
          this.getTableInvCl().setEdit(true);
        }
        else {
          this.getTableInv().display(this.mvDisp, this.m_saCond[12]);
          this.getTableInv().setVisible(true);
          this.getTableInvCl().setVisible(false);
          this.getTableInv().setEdit(true);
        } // if

        String[] args = new String[1];
        args[0] = String.valueOf(this.mvDisp.size());
        String message = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20146020", "UPP20146020-000028", null, args);
        /* @res "���β�ѯ�����{0}������" */
        ;
        this.showHintMessage(message);
      }
      else { // �����Ϊ�գ�����ս��� by ��־�� 2005-11-06
        this.getTableInv().display(null, this.m_saCond[12]);
        this.getTableInvCl().display(null, this.m_saCond[12]);
        this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
            "UPP20146020-000017") /* @res "û�з�������������" */);
      }
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(01-4-25 9:48:15)
   */

  public void onOkButtonClicked() {
    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000016") /* @res "��ȴ�" */);
    try {
      // �Ӵ�������������л������
      // m_saCond = getPriceAllocPanel1().getcondition();
      this.m_saCond = this.m_pnlcondition.getConditions();
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    this.m_sa1 = new String[10];
    this.m_sa1[0] = this.m_sCorp; // ��¼��λ
    this.m_sa1[1] = this.m_dLogindata.toString(); // ��¼����
    this.m_sa1[2] = this.m_sAccountYear; // ��¼�����
    this.m_sa1[3] = this.m_sAccountMonth; // ��¼�����
    this.m_sa1[4] = this.m_dMonthBeginDate.toString(); // ��ǰ����¿�ʱ��
    this.m_sa1[5] = this.m_dMonthEndDate.toString(); // ��ǰ����½�����
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
      this.showHintMessage(e.getMessage());
      return;
    }
    catch (Exception e) {
      e.printStackTrace();
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000013") /* @res "�쳣����" */);
      return;
    }
    if ((this.mvQry != null) && (this.mvQry.size() != 0)) {
      // ���ð�ť�仯
      // m_btnOk.setEnabled( false );
      this.m_btnCancel.setEnabled(true);
      this.m_btnAlloc.setEnabled(true);
      this.m_btnPrint.setEnabled(true);
      this.setButtons(this.m_aryButtonGroup);

      this.mvDisp = null;
      this.mvDisp = this.mvQry;
      // �����Ƿ��մ��������гɱ�������ʾ����
      if (this.m_saCond[12].equals("T")) {
        this.getTableInvCl().display(this.mvDisp, this.m_saCond[12]);
        this.getTableInvCl().setVisible(true);
        this.getTableInvCl().setEdit(true);
      }
      else {
        this.getTableInv().display(this.mvDisp, this.m_saCond[12]);
        this.getTableInv().setVisible(true);
        this.getTableInv().setEdit(true);
      } // if
      // getPriceAllocPanel1().setVisible( false );

      String[] args = new String[1];
      args[0] = String.valueOf(this.mvDisp.size());
      String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000028", null, args);
      /* @res "���β�ѯ�����{0}������" */
      ;
      this.showHintMessage(message);
    }
    else {
      this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
          "UPP20146020-000017") /* @res "û�з�������������" */);
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�01-4-25 9:54:53)
   */
  private void onPrintButtonClicked() {
    String[][] sa2ColName = null;
    Vector v = null;
    Object[][] oa2p = null;
    int[] iaColWidth = null;
    int[] iaFlag = null; //
    if (this.m_saCond[12].equals("T")) {
      sa2ColName = new String[][] {
        {
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001449") /* @res "����������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001446") /* @res "�����������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("2014",
                "UPP2014-000649") /* @res "����������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001945") /* @res "�ܳɱ����" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001775")
        /* @res "ƽ������" */}
      };
      v = this.getTableInvCl().getdata();

      oa2p = new Object[v.size()][5];
      int iCount = v.size();
      for (int i = 0; i < iCount; i++) {
        for (int j = 0; j < 5; j++) {
          oa2p[i][j] = ((Vector) v.elementAt(i)).elementAt(j);
        } // for
      }
      iaColWidth = new int[] {
          100, 100, 100, 100, 100
      };
      iaFlag = new int[] {
          0, 0, 2, 2, 2
      }; //
    }
    else {
      sa2ColName = new String[][] {
        {
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0002930") /* @res "��������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001480") /* @res "�������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001453") /* @res "�������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0003448") /* @res "���" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001240") /* @res "�ͺ�" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("2014",
                "UPP2014-000649") /* @res "����������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001945") /* @res "�ܳɱ����" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
                "UC000-0001775")
        /* @res "ƽ������" */}
      };
      v = this.getTableInv().getdata();
      oa2p = new Object[v.size()][8];
      int iCount = v.size();
      for (int i = 0; i < iCount; i++) {
        for (int j = 0; j < 8; j++) {
          oa2p[i][j] = ((Vector) v.elementAt(i)).elementAt(j);
        } // for
      }
      iaColWidth = new int[] {
          100, 80, 80, 80, 80, 90, 90, 90
      };
      iaFlag = new int[] {
          0, 0, 0, 0, 0, 2, 2, 2
      }; //
    } // if
    String sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000008") /* @res "����Ʒ���ɱ�����" */;

    String[] args = new String[3];
    args[0] = this.m_sCorpName;
    args[1] = this.m_sAccountYear;
    args[2] = this.m_sAccountMonth;
    String sTopstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000029", null, args);
    /* @res "��˾�� {0} ����ڼ䣺 {1}-{2}" */
    ;

    args = new String[2];
    args[0] = this.m_voUser.getUserName();
    args[1] = this.m_dLogindata.toString();
    String sBotstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
        "UPP20146020-000030", null, args);
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
    if (this.m_saCond[12].equals("T")) { // ���մ��������з���
    }
    else {
    } // if
  }

  /**
   * ��������: ����: ����ֵ: �쳣:
   * 
   * @return boolean
   * @param v
   *          java.util.Vector
   */
  private Vector checkdata(Vector v) {
    Vector data = new Vector();
    String sTmp = null;
    int iCount = v.size();
    int iPos;

    // �Ƿ��մ��������гɱ�����
    if (this.m_saCond[12].equals("T")) {
      iPos = 4;
    }
    else {
      iPos = 7;
    } // if
    for (int i = 0; i < iCount; i++) {
      boolean canAlloc = true;
      if (((Vector) v.elementAt(i)).elementAt(iPos) == null) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        // m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
        // "20146020", "UPP20146020-000024", null, args );
        // /*@res "��{0}��ƽ������Ϊ�գ�������"*/
        ;
        canAlloc = false;
      }
      sTmp = ((Vector) v.elementAt(i)).elementAt(iPos).toString();
      String sTmpMny = ((Vector) v.elementAt(i)).elementAt(iPos - 1).toString();
      if (sTmp.length() == 0) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        // m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
        // "20146020", "UPP20146020-000024", null, args );
        /* @res "��{0}��ƽ������Ϊ�գ�������" */
        canAlloc = false;

        // Ϊ�ղ���ʾ������
      }
      if (new nc.vo.pub.lang.UFDouble(sTmp).doubleValue() < 0) {
        String[] args = new String[1];
        args[0] = String.valueOf(i + 1);
        this.m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20146020",
            "UPP20146020-000025", null, args);
        /* @res "��{0}��ƽ������С���㣬������" */
        ;
        canAlloc = false;

        return null;
      }
      if (new nc.vo.pub.lang.UFDouble(sTmpMny).doubleValue() > this.m_dMaxValue) {

        String[] value = new String[] {
          String.valueOf(this.m_dMaxValue)
        };
        this.m_sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000164", null, value);
        canAlloc = false;

        return null;
      }
      if (canAlloc) {
        data.add(v.elementAt(i));
      }
    }
    return data;
  }

  /**
   * ���� PrAllocTabPanel1 ����ֵ��
   * 
   * @return nc.ui.ia.pricealloc.PrAllocTabPanel
   */
  /* ���棺�˷������������ɡ� */
  private PnlInv getTableInv() {
    if (this.m_pnlTblInv == null) {
      try {
        this.m_pnlTblInv = new PnlInv();
        this.m_pnlTblInv.setName("PnlInv");
      }
      catch (java.lang.Throwable ivjExc) {
        this.handleException(ivjExc);
      }
    }
    return this.m_pnlTblInv;
  }

  /**
   * ���� PrAllocTabPanel1 ����ֵ��
   * 
   * @return nc.ui.ia.pricealloc.PrAllocTabPanel
   */
  /* ���棺�˷������������ɡ� */
  private PnlInvCl getTableInvCl() {
    if (this.m_pnlTblInvCl == null) {
      try {
        this.m_pnlTblInvCl = new PnlInvCl();
        this.m_pnlTblInvCl.setName("m_pnlTblInvCl");
      }
      catch (java.lang.Throwable ivjExc) {
        this.handleException(ivjExc);
      }
    }
    return this.m_pnlTblInvCl;
  }
}
