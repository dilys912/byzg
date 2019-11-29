package nc.ui.so.pub;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.dbcache.gui.MessageBox;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.PrintEntry;
import nc.ui.so.so002.SaleinvoiceBO_Client;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;
import nc.vo.so.pub.FpylVO;
import nc.vo.so.pub.FpylbVO;
import nc.vo.so.pub.FpylhVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class InvoiceOutToTxt
{
  Hashtable hscustaddr = new Hashtable();
  Hashtable hsbank = new Hashtable();
  Hashtable hsbankno = new Hashtable();
  Hashtable hsinvName = new Hashtable();
  Hashtable hsinvspec = new Hashtable();
  Hashtable hsmeasname = new Hashtable();
  Hashtable hsinvcl = new Hashtable();
  Hashtable hspk_measure = new Hashtable();
  Hashtable hsinvclasscode = new Hashtable();
  Hashtable hsinvclassname = new Hashtable();
  Hashtable hscustInfo = new Hashtable();
  String sDiv = " ";

  int digitmny = 2;
  int digitnum = 6;
  String invcode = null;

  private String def1 = null;
  private String vdef2 = null;

  protected PrintEntry m_print = null;
  private CardPanelPRTS m_dataSource = null;

  public String covNULL(Object o)
  {
    if ((o == null) || (o.toString().trim().equals("")))
      return "\"\"";
    return o.toString();
  }

  public String covString(Object o)
  {
    if ((o == null) || (o.toString().trim().equals(""))) {
      return "\"\"";
    }
    return "\"" + o.toString() + "\"";
  }

  public boolean inputFile(String[] str)
  {
    FileDialog OutFileDialog = new FileDialog(new Frame());
    OutFileDialog.setName("OutFileDialog");
    OutFileDialog.setLayout(null);
    OutFileDialog.setMode(1);
    OutFileDialog.setFile("fp.txt");
    OutFileDialog.show();

    if ((OutFileDialog.getDirectory() != null) && (OutFileDialog.getFile() != null)) {
      String sFileName = OutFileDialog.getDirectory() + OutFileDialog.getFile();
      if (sFileName.indexOf(".") == -1)
        sFileName = sFileName + ".TXT";
      System.out.println(sFileName);
      try {
        RandomAccessFile file = new RandomAccessFile(sFileName, "rw");
        file.setLength(0L);
        long length = file.length();
        file.seek(length);
        for (int i = 0; i < str.length; i++)
          if (str[i] != null)
            file.write(str[i].getBytes());
        file.close();
        return true;
      } catch (Exception e) {
        System.out.println(e.toString());
        return false;
      }
    }
    return false;
  }

  public boolean outputInvoice(SaleinvoiceVO[] vos, UFBoolean isUseViaNum, String sAudit, String sPay)
  {
    String[] toFile = (String[])null;
    String[] batchToFiles = (String[])null;
    ArrayList batchToFileArray = new ArrayList();

    String sTaxType = "按存货合并";
    String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    try
    {
      SaleinvoiceVO[] vostmp = (SaleinvoiceVO[])null;
      if (("按客户+存货合并".equals(sTaxType)) || ("按客户+存货类合并".equals(sTaxType)))
      {
        Hashtable hsnew = new Hashtable();
        for (int i = 0; i < vos.length; i++);
        Enumeration keys = hsnew.keys();
        Vector vt = new Vector();
        while (keys.hasMoreElements()) {
          SaleinvoiceVO vo = (SaleinvoiceVO)hsnew.get((String)keys.nextElement());
          vt.add(vo);
        }
        vostmp = new SaleinvoiceVO[vt.size()];
        vt.copyInto(vostmp);
      } else {
        for (int i = 0; i < vos.length; i++) {
          SaleinvoiceVO vo = vos[i];
          SaleVO head = (SaleVO)vo.getParentVO();
          SaleinvoiceBVO[] items = (SaleinvoiceBVO[])vo.getChildrenVO();
          Vector vt = new Vector();
          for (int j = 0; j < items.length; j++)
            if ((items[j].getBlargessflag() == null) || (!items[j].getBlargessflag().booleanValue()))
            {
              vt.add(items[j]);
            }
          SaleinvoiceBVO[] itemsall = new SaleinvoiceBVO[vt.size()];
          vt.copyInto(itemsall);
          vo.setChildrenVO(itemsall);
        }
        vostmp = vos;
      }
      for (int m = 0; m < vostmp.length; m++) {
        SaleinvoiceVO vo = vostmp[m];
        SaleVO head = (SaleVO)vo.getParentVO();
        SaleinvoiceBVO[] items = (SaleinvoiceBVO[])vo.getChildrenVO();
        Hashtable hsinv = new Hashtable();
        Hashtable hsinvcl = new Hashtable();

        UFDouble disall = new UFDouble(0);
        UFDouble disalltax = new UFDouble(0);
        UFDouble allmny = new UFDouble(0);
        for (int i = 0; i < items.length; i++)
        {
          disall = disall.add(items[i].getNoriginalcurdiscountmny());
          disalltax = disalltax.add(items[i].getNoriginalcurdiscountmny().sub(items[i].getNoriginalcurdiscountmny().div(items[i].getNtaxrate().div(100.0D).add(1.0D))));
          items[i].setNoriginalcursummny(add(add(items[i].getNoriginalcursummny(), items[i].getNoriginalcurdiscountmny()), items[i].getSjtsje() == null ? new UFDouble("0") : items[i].getSjtsje()));
          allmny = allmny.add(items[i].getNoriginalcursummny());
          items[i].setNoriginalcurmny(div(items[i].getNoriginalcursummny(), items[i].getNtaxrate().div(100.0D).add(1.0D)));
          items[i].setNoriginalcurtaxmny(items[i].getNoriginalcursummny().sub(items[i].getNoriginalcurmny()));

          items[i].setNitemdiscountrate(new UFDouble(0));
        }

        HashMap map = new HashMap();
        int row = 1;
        for (int i = 0; i < items.length; i++) {
          if ((items[i].getNnumber() != null) && (items[i].getNoriginalcursummny() != null))
          {
            String[] srows = new String[5];
            if (("按存货合并".equals(sTaxType)) || ("按客户+存货合并".equals(sTaxType))) {
              if (hsinv.get(items[i].getCinventoryid()) != null) {
                SaleinvoiceBVO body = (SaleinvoiceBVO)((SaleinvoiceBVO)hsinv.get(items[i].getCinventoryid())).clone();
                body.setNpacknumber(add(body.getNpacknumber(), items[i].getNpacknumber()));
                if (body.getNpacknumber() != null) body.setNpacknumber(body.getNpacknumber());
                body.setNnumber(add(body.getNnumber(), items[i].getNnumber()));
                body.setNoriginalcursummny(add(body.getNoriginalcursummny(), items[i].getNoriginalcursummny()));
                body.setNoriginalcurdiscountmny(add(body.getNoriginalcurdiscountmny(), items[i].getNoriginalcurdiscountmny()));
                if (body.getNoriginalcurdiscountmny() != null) body.setNoriginalcurdiscountmny(body.getNoriginalcurdiscountmny());
                System.out.println(i + "====" + body.getNoriginalcurtaxmny() + "||||" + items[i].getNoriginalcurtaxmny());

                body.setNoriginalcurtaxmny(add(body.getNoriginalcurtaxmny(), items[i].getNoriginalcurtaxmny()));
                body.setNoriginalcurmny(add(body.getNoriginalcurmny(), items[i].getNoriginalcurmny()));
                body.setNitemdiscountrate(div(body.getNoriginalcurdiscountmny(), add(body.getNoriginalcursummny(), body.getNoriginalcurdiscountmny())).multiply(-100.0D).add(100.0D));
                body.setNtaxrate(body.getNoriginalcurtaxmny().multiply(100.0D).div(body.getNoriginalcurmny()));

                hsinv.put(items[i].getCinventoryid(), body);
                String[] s = (String[])map.get(body.getCinventoryid());
                srows[0] = String.valueOf(Integer.valueOf(s[0]) + 1);
                srows[1] = (body.getHttsbl() + "%");
                srows[2] = String.valueOf(body.getZtsje().add(items[i].getZtsje()));

                map.put(items[i].getCinventoryid(), srows);
              } else {
                hsinv.put(items[i].getCinventoryid(), items[i]);
                srows[0] = String.valueOf(row);
                srows[4] = items[i].getCinventoryid();

                map.put(items[i].getCinventoryid(), srows);
              }
            }
            else if (("按存货类合并".equals(sTaxType)) || ("按客户+存货类合并".equals(sTaxType))) {
              String invcl = FetchValueBO_Client.getColValue("bd_invbasdoc", "pk_invcl", "pk_invbasdoc='" + items[i].getCinvbasdocid() + "'");

              if (hsinvcl.get(invcl) != null) {
                SaleinvoiceBVO body = (SaleinvoiceBVO)((SaleinvoiceBVO)hsinvcl.get(invcl)).clone();
                body.setNpacknumber(add(body.getNpacknumber(), items[i].getNpacknumber()));
                if (body.getNpacknumber() != null) body.setNpacknumber(body.getNpacknumber());
                body.setNnumber(add(body.getNnumber(), items[i].getNnumber()));
                body.setNoriginalcursummny(add(body.getNoriginalcursummny(), items[i].getNoriginalcursummny()));
                body.setNoriginalcurdiscountmny(add(body.getNoriginalcurdiscountmny(), items[i].getNoriginalcurdiscountmny()));
                if (body.getNoriginalcurdiscountmny() != null) body.setNoriginalcurdiscountmny(body.getNoriginalcurdiscountmny());
                body.setNoriginalcurtaxmny(add(body.getNoriginalcurtaxmny(), items[i].getNoriginalcurtaxmny()));
                body.setNitemdiscountrate(div(body.getNoriginalcurdiscountmny(), add(body.getNoriginalcursummny(), body.getNoriginalcurdiscountmny())).multiply(-100.0D).add(100.0D));
                body.setNoriginalcurmny(add(body.getNoriginalcurmny(), items[i].getNoriginalcurmny()));
                body.setNtaxrate(body.getNoriginalcurtaxmny().multiply(100.0D).div(body.getNoriginalcurmny()));
                hsinvcl.put(invcl, body);
              } else {
                hsinvcl.put(invcl, items[i]);
              }

            }

          }

        }

        String vnote = "0";
        if ((head.getVnote() != null) && 
          (!head.getVnote().equals(""))) {
          vnote = head.getVnote().toString();
          if(vnote.indexOf("元")>0){        //直截取备注如果备注中不包含元会报错所以加了判断
        	  vnote = vnote.substring(0, vnote.indexOf("元"));
          }
              vnote = vnote.replaceAll("[^0-9]", "");
        }

        toFile = new String[1 + items.length];

        StringBuffer str = new StringBuffer();

        str.append(head.getVreceiptcode());

        int rowcount = 0;
        if ((("按存货合并".equals(sTaxType)) || ("按客户+存货合并".equals(sTaxType))) && (hsinv.size() > 0)) {
          rowcount = hsinv.size();
        }
        else if ((("按存货类合并".equals(sTaxType)) || ("按客户+存货类合并".equals(sTaxType))) && (hsinvcl.size() > 0))
          rowcount = hsinvcl.size();
        else rowcount = items.length;

        str.append(this.sDiv + rowcount);

        String[][] custInfo = (String[][])this.hscustInfo.get(head.getCreceiptcorpid());
        if (custInfo == null) {
          custInfo = SaleinvoiceBO_Client.getCustomerInfo(head.getCreceiptcorpid());
          this.hscustInfo.put(head.getCreceiptcorpid(), custInfo);
        }
        String pk_cumandoc = head.getCreceiptcorpid();
        if (pk_cumandoc != null) {
          String vdef1 = FetchValueBO_Client.getColValue("bd_cumandoc", "def1", "pk_cumandoc='" + pk_cumandoc + "'");
          this.vdef2 = FetchValueBO_Client.getColValue("bd_cumandoc", "def2", "pk_cumandoc='" + pk_cumandoc + "'");
          this.def1 = FetchValueBO_Client.getColValue("bd_defdoc", "doccode", "pk_defdoc='" + vdef1 + "'");
        }
        str.append(this.sDiv + covString(custInfo[0][6]));

        str.append(this.sDiv + covString(custInfo[0][1]));

        String where = "pk_cubasdoc='" + custInfo[0][5] + "'";
        String custaddr = (String)this.hscustaddr.get(custInfo[0][5]);
        if (custaddr == null) {
          custaddr = FetchValueBO_Client.getColValue("bd_cubasdoc", "conaddr", where);
          if (custaddr != null)
            this.hscustaddr.put(custInfo[0][5], custaddr);
          else {
            this.hscustaddr.put(custInfo[0][5], "");
          }

        }

        str.append(this.sDiv + covString(custaddr == null ? "" : new StringBuilder().append(custaddr).append(this.sDiv).append(custInfo[0][0]).toString()));

        String bank = (String)this.hsbank.get(custInfo[0][5]);
        if (bank == null) {
          bank = FetchValueBO_Client.getColValue("bd_custbank", "accname", "pk_cubasdoc='" + custInfo[0][5] + "' and defflag = 'Y' and (pk_corp='" + pk_corp + "' or pk_corp = '0001') order by pk_corp desc");

          if (bank != null)
            this.hsbank.put(custInfo[0][5], bank);
          else
            this.hsbank.put(custInfo[0][5], "");
        }
        String bankno = (String)this.hsbankno.get(custInfo[0][5]);
        if (bankno == null) {
          bankno = FetchValueBO_Client.getColValue("bd_custbank", "account", "pk_cubasdoc='" + custInfo[0][5] + "' and defflag = 'Y' and (pk_corp='" + pk_corp + "' or pk_corp = '0001') order by pk_corp desc");

          if (bankno != null)
            this.hsbankno.put(custInfo[0][5], bankno);
          else {
            this.hsbankno.put(custInfo[0][5], "");
          }
        }

        str.append(this.sDiv + covString(new StringBuilder().append(bank == null ? "" : bank).append(this.sDiv).append(bankno == null ? "" : bankno).toString()));

        StringBuffer str2 = new StringBuffer();

        int falg = 0;
        if (("按存货合并".equals(sTaxType)) && (hsinv.size() > 0))
        {
          String snoriginalcurdiscountmny2 = null;

          Set<String> set = hsinv.keySet();
          UFDouble countgshz = new UFDouble(0);
          UFDouble countgshy = new UFDouble(0);
          UFDouble countgghz = new UFDouble(0);
          UFDouble countgghy = new UFDouble(0);
          DecimalFormat df = new DecimalFormat("#");
          DecimalFormat df1 = new DecimalFormat("#.00");
          String httsbls = "";
          String httsblg = "";
          UFDouble nnumbers = new UFDouble(0);
          UFDouble nnumberg = new UFDouble(0);
          UFDouble noriginalcurtaxprices = new UFDouble(0);
          UFDouble noriginalcurtaxpriceg = new UFDouble(0);
          for (String s : set) {
            SaleinvoiceBVO body = (SaleinvoiceBVO)((SaleinvoiceBVO)hsinv.get(s)).clone();

            String pk_invbasdoc = FetchValueBO_Client.getColValue("bd_invmandoc", "pk_invbasdoc", "pk_invmandoc='" + s + "'");
            String invcode = FetchValueBO_Client.getColValue("bd_invbasdoc", "invcode", "pk_invbasdoc='" + pk_invbasdoc + "'");
            String pk_invcl = FetchValueBO_Client.getColValue("bd_invbasdoc", "pk_invcl ", "pk_invbasdoc='" + pk_invbasdoc + "'");
            String invclasscode = FetchValueBO_Client.getColValue("bd_invcl", "invclasscode ", "pk_invcl='" + pk_invcl + "'");
            String csaleid = FetchValueBO_Client.getColValue("so_saleinvoice_b", "csaleid", "cinvoice_bid='" + body.getCinvoice_bid() + "'");
            snoriginalcurdiscountmny2 = FetchValueBO_Client.getColValue("so_saleinvoice_b", "sum(noriginalcurdiscountmny)", "csaleid='" + csaleid + "' and cinventoryid='" + s + "'");
            String nnumber = FetchValueBO_Client.getColValue("so_saleinvoice_b", "sum(nnumber)", "csaleid='" + csaleid + "' and cinventoryid='" + s + "'");
            String shttsbl = FetchValueBO_Client.getColValue("so_saleinvoice_b", "max(httsbl)", "csaleid='" + csaleid + "' and cinventoryid='" + s + "' ");

            String ndiscountmny = FetchValueBO_Client.getColValue("so_saleinvoice_b", "sum(ndiscountmny)", "csaleid='" + csaleid + "' and cinventoryid='" + s + "' ");

            String sjtsje = FetchValueBO_Client.getColValue("so_saleinvoice_b", "sum(sjtsje)", "csaleid='" + csaleid + "' and cinventoryid='" + s + "' ");
            String ztsje = FetchValueBO_Client.getColValue("so_saleinvoice_b", "sum(ztsje)", "csaleid='" + csaleid + "' and cinventoryid='" + s + "' ");
            UFDouble zke = new UFDouble(ztsje);

            UFDouble d1 = new UFDouble(ndiscountmny);
            UFDouble d2 = new UFDouble(sjtsje);
            if (zke.sub(d1.add(d2)).doubleValue() != 0.0D) {
              String vreceiptcode = FetchValueBO_Client.getColValue("so_saleinvoice", "vreceiptcode", "csaleid ='" + csaleid + "'");
              MessageBox.showMessageDialog("单据" + vreceiptcode + "总途损金额异常,请查验后再进行开票业务!", "数据异常");
              return false;
            }

            if (!snoriginalcurdiscountmny2.equals("0")) {
              UFDouble count = new UFDouble(nnumber).multiply(new UFDouble(shttsbl)).div(100.0D);

              if (invclasscode.substring(0, 2).equals("23")) {
                falg = 1;
                httsbls = shttsbl;
                nnumbers = nnumbers.add(new UFDouble(nnumber));
                noriginalcurtaxprices = body.getNoriginalcurtaxprice();
              }

              if ((invclasscode.substring(0, 2).equals("22")) || (invclasscode.substring(0, 2).equals("21"))) {
                falg = 1;
                httsblg = shttsbl;
                nnumberg = nnumberg.add(new UFDouble(nnumber));
                noriginalcurtaxpriceg = body.getNoriginalcurtaxprice();
              }

            }

            body.setNoriginalcurdiscountmny(zke);
            hsinv.put(s, body);
          }
          if (falg == 1)
          {
            if ((!nnumbers.equals(new UFDouble(0))) || (!noriginalcurtaxprices.equals(new UFDouble(0))) || (!httsbls.equals(""))) {
              countgshz = nnumbers.multiply(new UFDouble(httsbls)).div(100.0D);
              countgshy = countgshz.multiply(noriginalcurtaxprices);
              str2.append("罐身-计价途损数量：" + df.format(countgshz) + "只 金额：" + df1.format(countgshy) + "元 (含税)\\n");
            }
            if ((!nnumberg.equals(new UFDouble(0))) || (!noriginalcurtaxpriceg.equals(new UFDouble(0))) || (!httsblg.equals(""))) {
              countgghz = nnumberg.multiply(new UFDouble(httsblg)).div(100.0D);
              countgghy = countgghz.multiply(noriginalcurtaxpriceg);
              str2.append("罐盖-计价途损数量：" + df.format(countgghz) + "只 金额：" + df1.format(countgghy) + "元 (含税)\\n");
            }

          }

        }
        //  String sysinit = SysInitBO_Client.getPkValue(pk_corp, "SO65");
        //2014-10-15 ljy  start
        UFBoolean sysinit= SysInitBO_Client.getParaBoolean(pk_corp, "SO65");
        // 2014-10-15 ljy  end
       
        if(sysinit.booleanValue()){
        if (((head.getVnote() != null) && (head.getVnote().length() != 0)) || (falg == 1))
        {
          if (head.getVnote() != null){       
        	  str.append(this.sDiv + covString(str2.append(this.sDiv + head.getVnote() == null ? "" : new StringBuilder(String.valueOf(head.getVnote())).append("\\n").toString()).append("(含税)")));
              
          } else {
            str.append(this.sDiv + covString(str2));
          }
        }
        else
        {
        	str.append(this.sDiv + covString("(含税)"));
          
        }
        }
        else{
          str2.setLength(0);
   		  str.append(this.sDiv + covString(str2.append(this.sDiv + head.getVnote() == null ? "" : new StringBuilder(String.valueOf(head.getVnote())).append("\\n").toString())));         
        
        }
        String capprovename = null;
        if (head.getCapproveid() != null) {
          capprovename = FetchValueBO_Client.getColValue("sm_user", "user_name", " nvl(dr,0)=0 and cuserid = '" + head.getCapproveid() + "'");
          if (capprovename != null)
            str.append(this.sDiv + covString(capprovename));
        }
        else {
          str.append(this.sDiv + covString(""));
        }

        toFile[0] = (str.toString() + "\r\n");

        for (int i = 0; i < items.length; i++) {
          if ((items[i].getNoriginalcursummny() != null) && 
            (!"按存货合并".equals(sTaxType)) && (!"按客户+存货合并".equals(sTaxType)) && (!"按存货类合并".equals(sTaxType)) && (!"按客户+存货类合并".equals(sTaxType)))
          {
            str = RowToStr(items[i], isUseViaNum, sTaxType);
            toFile[(i + 1)] = (str.toString() + "\r\n");
          }

        }

        int irec = 0;
        if ((("按存货合并".equals(sTaxType)) || ("按客户+存货合并".equals(sTaxType))) && (hsinv.size() > 0)) {
          Enumeration keys = hsinv.keys();
          while (keys.hasMoreElements()) {
            SaleinvoiceBVO body = (SaleinvoiceBVO)hsinv.get((String)keys.nextElement());

            str = RowToStr(body, isUseViaNum, sTaxType);
            toFile[(irec + 1)] = (str.toString() + "\r\n");
            irec++;
          }
        } else if ((("按存货类合并".equals(sTaxType)) || ("按客户+存货类合并".equals(sTaxType))) && (hsinvcl.size() > 0)) {
          Enumeration keys = hsinvcl.keys();
          while (keys.hasMoreElements()) {
            SaleinvoiceBVO body = (SaleinvoiceBVO)hsinvcl.get((String)keys.nextElement());

            str = RowToStr(body, isUseViaNum, sTaxType);
            toFile[(irec + 1)] = (str.toString() + "\r\n");
            irec++;
          }

        }

        for (int n = 0; n < toFile.length; n++) {
          batchToFileArray.add(toFile[n]);
        }

      }

      batchToFiles = new String[batchToFileArray.size()];
      batchToFileArray.toArray(batchToFiles);
    } catch (Exception e1) {
      System.out.println("查询失败！");
      e1.printStackTrace(System.out);
    }

    printview(batchToFiles);

    return inputFile(batchToFiles);
  }

  private void printview(String[] strs)
  {
   /// String pk_corp = "1016";
	  String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();//获取当前公司
    String checker = "";
    this.m_print = new PrintEntry(null, null);
    FpylVO[] vos = str2VOs(strs);
    for (FpylVO vo : vos) {
      this.m_print.setTemplateID(pk_corp, "40060502", ClientEnvironment.getInstance().getUser().getPrimaryKey(), null, "fpyl_z2");

      BillCardPanel cp = new BillCardPanel();
      cp.loadTemplet("fpyl", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), pk_corp);
      cp.setBillValueVO(vo);

      this.m_dataSource = new CardPanelPRTS("test123", cp);
      this.m_print.setDataSource(this.m_dataSource);
      this.m_dataSource.getAllDataItemNames();
      this.m_print.getDataSource();
    }
    this.m_print.previewInDialog(true);
  }

  private FpylVO[] str2VOs(String[] allstrs)
  {
	    
    int line = 0;
    String tempString = null;
    List list = new ArrayList();
    List blist = new ArrayList();
    FpylhVO hvo = null;
    FpylbVO bvo = null;
    for (int i = 0; i < allstrs.length; i++) {
      tempString = allstrs[i];
      if ((tempString != null) && (!"".equals(tempString)))
      {
        if (line == 0) {
          hvo = new FpylhVO();
          String[] strs = tempString.split("\"");
          String[] temps = strs[0].split(" ");
          line = Integer.parseInt(temps[1]);

          hvo.setDjh(temps[0]);

          hvo.setZbhsl(temps[1]);

          hvo.setKhmc(strs[1]);

          hvo.setNsrsbh(strs[3]);

          hvo.setDz(strs[5]);

          hvo.setKhh(strs[7]);
        
		  hvo.setBz(strs[9].substring(0, strs[9].length() - 2));
		
          hvo.setFhr(strs[11]);
        }
        else
        {
          bvo = new FpylbVO();
          String[] strs = tempString.split("\"");

          bvo.setLh(strs[1]);

          bvo.setDw(strs[3]);

          bvo.setGg(strs[5]);

          String[] temps = strs[6].split(" ");

          bvo.setSl(new UFDouble(temps[1]).setScale(0, 4).toString());

          bvo.setJshj(temps[2]);

          UFDouble jshj = new UFDouble(temps[2]);
          UFDouble se = new UFDouble(temps[6].substring(0, temps[6].length() - 2));
          UFDouble sl = new UFDouble(temps[1]);
          bvo.setDj(jshj.sub(se).div(sl).toString());

          bvo.setJe(jshj.sub(se).setScale(2, 4).toString());

          bvo.setShuilv(temps[3]);

          bvo.setSpsm(temps[4]);

          bvo.setZkje(temps[5]);

          bvo.setSe(temps[6]);
          blist.add(bvo);

          FpylbVO bvo2 = new FpylbVO();

          UFDouble zkje = new UFDouble(temps[5]);
          UFDouble shuilv = new UFDouble(temps[3]);
          UFDouble je = new UFDouble("0").sub(zkje.div(new UFDouble("1").add(shuilv))).setScale(2, 4);
          bvo2.setJe(je.toString());

          bvo2.setSe(new UFDouble("0").sub(zkje.add(je)).setScale(2, 4).toString());

          UFDouble bl = zkje.div(jshj).multiply(100.0D).setScale(3, 1);
          bvo2.setLh("折扣(" + bl + "%)");
          blist.add(bvo2);

          line--;
          if (line == 0) {
            FpylVO vo = new FpylVO();
            vo.setParentVO(hvo);
            FpylbVO[] bvos = new FpylbVO[blist.size()];
            vo.setChildrenVO((FpylbVO[])blist.toArray(bvos));
            list.add(vo);
            blist.clear();
          }
        }
      }
    }
    FpylVO[] vos = new FpylVO[list.size()];
    return (FpylVO[])list.toArray(vos);
  }

  private UFDouble add(UFDouble d1, UFDouble d2)
  {
    if (d1 == null) return d2;
    if (d2 == null) return null;
    return d1.add(d2);
  }
  private UFDouble div(UFDouble d1, UFDouble d2) {
    if (d1 == null) return null;
    if (d2 == null) return null;
    return d1.div(d2);
  }
  private StringBuffer RowToStr(SaleinvoiceBVO row, UFBoolean isUseViaNum, String sTaxType) {
    StringBuffer str = new StringBuffer();
    try {
      String invName = (String)this.hsinvName.get(row.getCinvbasdocid());
      if (invName == null) {
        String invCode = FetchValueBO_Client.getColValue("bd_invbasdoc", "invcode", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");
        String invname = FetchValueBO_Client.getColValue("bd_invbasdoc", "invname", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");
        String pk_invcl = FetchValueBO_Client.getColValue("bd_invbasdoc", "pk_invcl ", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");
        String invclasscode = FetchValueBO_Client.getColValue("bd_invcl", "invclasscode ", "pk_invcl='" + pk_invcl + "'");
        invclasscode = invclasscode.substring(0, 6);
        if (invclasscode.substring(0, 2).equals("23")) {
          invName = FetchValueBO_Client.getColValue("bd_invcl", "invclassname", "invclasscode='" + invclasscode + "'");
          invName = invName.substring(0, 12);
        } else {
          invName = invname;
        }
        this.hsinvName.put(row.getCinvbasdocid(), invName);
      }

      String invspec = (String)this.hsinvspec.get(row.getCinvbasdocid());
      if (invspec == null) {
        String invCode = FetchValueBO_Client.getColValue("bd_invbasdoc", "invcode", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");
        String pk_invcl = FetchValueBO_Client.getColValue("bd_invbasdoc", "pk_invcl ", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");
        String invclasscode = FetchValueBO_Client.getColValue("bd_invcl", "invclasscode ", "pk_invcl='" + pk_invcl + "'");
        if (invclasscode.substring(0, 2).equals("23")) {
          if ("2".equals(this.def1)) {
            if ((this.vdef2 == null) || (this.vdef2.length() == 0))
              invspec = "饮料系列罐";
            else {
              invspec = this.vdef2;
            }
            this.hsinvspec.put(row.getCinvbasdocid(), invspec);
          } else {
            if (this.def1 == null) {
              MessageBox.showMessageDialog("当前单据中的客户在【客商管理档案】中未区分大小客户类型！请修改！！", "错误信息！");
            }

            String pk_defdoc = FetchValueBO_Client.getColValue("bd_invmandoc", "def12", " pk_corp = '" + ClientEnvironment.getInstance().getCorporation().getPk_corp() + "' and pk_invbasdoc='" + row.getCinvbasdocid() + "'");
            invspec = FetchValueBO_Client.getColValue("bd_defdoc", "docname", "pk_defdoc='" + pk_defdoc + "'");
            if (invspec != null)
              this.hsinvspec.put(row.getCinvbasdocid(), invspec);
            else
              this.hsinvspec.put(row.getCinvbasdocid(), "");
          }
        } else {
          invspec = FetchValueBO_Client.getColValue("bd_invbasdoc", "invspec", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");
          if (invspec != null)
            this.hsinvspec.put(row.getCinvbasdocid(), invspec);
          else {
            this.hsinvspec.put(row.getCinvbasdocid(), "");
          }
        }
      }

      String invcl = (String)this.hsinvcl.get(row.getCinvbasdocid());
      if (invcl == null) {
        invcl = FetchValueBO_Client.getColValue("bd_invbasdoc", "pk_invcl", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");

        this.hsinvcl.put(row.getCinvbasdocid(), invcl);
      }
      String measname = null;

      if (isUseViaNum.booleanValue()) {
        String pk_measure = (String)this.hspk_measure.get(row.getCinvbasdocid());
        if (pk_measure == null) {
          pk_measure = row.getCpackunitid();

          if (pk_measure != null)
            this.hspk_measure.put(row.getCinvbasdocid(), pk_measure);
        }
        if (pk_measure != null)
          measname = (String)this.hsmeasname.get(pk_measure);
        if (measname == null) {
          measname = FetchValueBO_Client.getColValue("bd_measdoc", "measname", "pk_measdoc='" + pk_measure + "'");

          if (measname != null) {
            this.hsmeasname.put(pk_measure, measname);
          }
        }
      }
      if ((!isUseViaNum.booleanValue()) || (measname == null)) {
        String pk_measure = (String)this.hspk_measure.get(row.getCinvbasdocid());
        if (pk_measure == null) {
          pk_measure = FetchValueBO_Client.getColValue("bd_invbasdoc", "pk_measdoc", "pk_invbasdoc='" + row.getCinvbasdocid() + "'");

          if (pk_measure != null)
            this.hspk_measure.put(row.getCinvbasdocid(), pk_measure);
        }
        if (pk_measure != null)
          measname = (String)this.hsmeasname.get(pk_measure);
        if (measname == null) {
          measname = FetchValueBO_Client.getColValue("bd_measdoc", "measname", "pk_measdoc='" + pk_measure + "'");
         //start lij 2014-10-27  销售发票开票传金税时PCS转换成个
          String dwgl=SysInitBO_Client.getParaString(row.getPk_corp(), "SO66");
          
      if(dwgl!=null && !dwgl.equals(":")){
          String[] dwgljx = dwgl.split("#");
          if(dwgljx.length==2){
        	if(dwgljx[0].equals("是")){
        		measname=dwgljx[1];
        	}
          }
          } 
        /*  if(measname.equals("pcs")){
        	  measname="个";
          }*/
          //end 
          if (measname != null) {
            this.hsmeasname.put(pk_measure, measname);
          }
        }
      }

      String invclasscode = (String)this.hsinvclasscode.get(row.getCinvbasdocid());
      if (invclasscode == null) {
        invclasscode = FetchValueBO_Client.getColValue("bd_invbasdoc,bd_taxitems", "taxcode", "bd_invbasdoc.pk_taxitems=bd_taxitems.pk_taxitems and pk_invbasdoc='" + row.getCinvbasdocid() + "'");

        this.hsinvclasscode.put(row.getCinvbasdocid(), invclasscode == null ? "" : invclasscode);
      }

      String invclassname = (String)this.hsinvclassname.get(invcl);
      if (invclassname == null) {
        invclassname = FetchValueBO_Client.getColValue("bd_invcl", "invclassname", "pk_invcl='" + invcl + "'");

        this.hsinvclassname.put(invcl, invclassname);
      }

      if (("按存货类合并".equals(sTaxType)) || ("按客户+存货类合并".equals(sTaxType)))
        str.append(covString(invclassname) + this.sDiv + covString(measname));
      else {
        str.append(covString(invName) + this.sDiv + covString(measname));
      }

      str.append(this.sDiv + covString(invspec));

      double number = 0.0D;
      if (isUseViaNum.booleanValue()) {
        if (row.getNpacknumber() != null)
          number = row.getNpacknumber().setScale(this.digitnum, 0).doubleValue();
      }
      else if (row.getNnumber() != null) {
        number = row.getNnumber().setScale(this.digitnum, 0).doubleValue();
      }
      str.append(this.sDiv + number);

      number = 0.0D;
      if (row.getNoriginalcurmny() != null)
        number = row.getNoriginalcursummny().setScale(this.digitmny, 0).doubleValue();
      str.append(this.sDiv + number);

      number = 0.0D;
      if (row.getNtaxrate() != null)
        number = row.getNtaxrate().div(100.0D).setScale(this.digitmny, 0).doubleValue();
      str.append(this.sDiv + number);

      this.invcode = invclasscode;
      str.append(this.sDiv + covNULL(invclasscode));

      if (row.getNoriginalcurdiscountmny() != null)
        str.append(this.sDiv + row.getNoriginalcurdiscountmny());
      else {
        str.append(this.sDiv + covNULL(null));
      }

      number = 0.0D;
      if (row.getNoriginalcurtaxmny() != null)
        number = row.getNoriginalcurtaxmny().setScale(this.digitmny, 0).doubleValue();
      str.append(this.sDiv + number);
    }
    catch (Exception e1)
    {
      System.out.println("查询失败！");
      e1.printStackTrace(System.out);
    }

    return str;
  }
  private StringBuffer conDiscountToStr(UFDouble allmny, UFDouble tax, UFDouble disMny) {
    StringBuffer str = new StringBuffer();

    str.append(covString(new StringBuilder().append("折扣(").append(disMny.div(allmny).multiply(100.0D).setScale(this.digitmny, 0).doubleValue()).append("%)").toString()) + this.sDiv + covString(null));

    str.append(this.sDiv + covString(null));

    str.append(this.sDiv + 0);

    str.append(this.sDiv + disMny.multiply(-1.0D).setScale(this.digitmny, 0).doubleValue());

    str.append(this.sDiv + tax.div(disMny.sub(tax)).setScale(this.digitmny, 0).doubleValue());

    str.append(this.sDiv + covNULL(this.invcode));

    str.append(this.sDiv + 0);

    str.append(this.sDiv + tax.multiply(-1.0D).setScale(this.digitmny, 0).doubleValue());

    str.append(this.sDiv + 0);

    return str;
  }
}