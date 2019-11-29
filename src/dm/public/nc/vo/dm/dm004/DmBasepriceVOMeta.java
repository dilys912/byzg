package nc.vo.dm.dm004;

import java.util.HashMap;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.pub.smart.SmartVOMeta;

public class DmBasepriceVOMeta extends SmartVOMeta
{
  public DmBasepriceVOMeta()
  {
    init();
  }

  private void init()
  {
    setTable("dm_baseprice");
    setLabel("dm_baseprice");
    setPkColName("pk_basicprice");
    SmartFieldMeta sfm = null;
    HashMap hmColumn = new HashMap();

    sfm = new SmartFieldMeta();
    sfm.setType(1);
    sfm.setDbType(2);
    sfm.setName("nuplimitnum");
    sfm.setColumn("nuplimitnum");
    sfm.setLabel("����ֵ");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    
    /******** add by yhj 2014-02-23 START ************/
	/** ��ע */
	sfm = new SmartFieldMeta();
	sfm.setType(SmartFieldMeta.JAVATYPE_STRING);
	sfm.setDbType(java.sql.Types.VARCHAR);
	sfm.setName("memo");
	sfm.setColumn("memo");
	sfm.setLabel("��ע");
	sfm.setColumnDef(null);
	sfm.setAllowNull(false);
	sfm.setPrecision(0);
	sfm.setLength(100);
	sfm.setPersistence(true);
	hmColumn.put(sfm.getName(), sfm);

	/******** add by yhj 2014-02-23 START ************/
    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pkfromarea");
    sfm.setColumn("pkfromarea");
    sfm.setLabel("����վ");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(0);
    sfm.setDbType(1);
    sfm.setName("bsltfrmlevel");
    sfm.setColumn("bsltfrmlevel");
    sfm.setLabel("ѯ��ʱ��ѯ���ۻ����������μ�");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(1);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_sendtype");
    sfm.setColumn("pk_sendtype");
    sfm.setLabel("���˷�ʽ����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pkroute");
    sfm.setColumn("pkroute");
    sfm.setLabel("·��");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_invclass");
    sfm.setColumn("pk_invclass");
    sfm.setLabel("�����������");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_vehicletype");
    sfm.setColumn("pk_vehicletype");
    sfm.setLabel("��������");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pktoarea");
    sfm.setColumn("pktoarea");
    sfm.setLabel("����վ");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(2);
    sfm.setDbType(5);
    sfm.setName("ipricetype");
    sfm.setColumn("ipricetype");
    sfm.setLabel("�Ƽ�����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(0);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_inventory");
    sfm.setColumn("pk_inventory");
    sfm.setLabel("�������");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pkfromaddress");
    sfm.setColumn("pkfromaddress");
    sfm.setLabel("�����ص�����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_transcontainer");
    sfm.setColumn("pk_transcontainer");
    sfm.setLabel("���������");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_transcust");
    sfm.setColumn("pk_transcust");
    sfm.setLabel("����������");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(2);
    sfm.setDbType(5);
    sfm.setName("iuplimittype");
    sfm.setColumn("iuplimittype");
    sfm.setLabel("��������");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(0);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(12);
    sfm.setName("vpriceunit");
    sfm.setColumn("vpriceunit");
    sfm.setLabel("�۸�λ");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(50);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pkpacksort");
    sfm.setColumn("pkpacksort");
    sfm.setLabel("��װ����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pktoaddress");
    sfm.setColumn("pktoaddress");
    sfm.setLabel("�����ص�����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pk_basicprice");
    sfm.setColumn("pk_basicprice");
    sfm.setLabel("���۱�����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pkdelivorg");
    sfm.setColumn("pkdelivorg");
    sfm.setLabel("������֯����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(1);
    sfm.setDbType(2);
    sfm.setName("noveruplmtprice");
    sfm.setColumn("noveruplmtprice");
    sfm.setLabel("�����޺�ļ۸�");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(1);
    sfm.setDbType(2);
    sfm.setName("dbaseprice");
    sfm.setColumn("dbaseprice");
    sfm.setLabel("����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);
    
    //eric
    sfm = new SmartFieldMeta();
    sfm.setType(1);
    sfm.setDbType(2);
    sfm.setName("taxprice");
    sfm.setColumn("taxprice");
    sfm.setLabel("��˰����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);
    
    sfm = new SmartFieldMeta();
    sfm.setType(1);
    sfm.setDbType(2);
    sfm.setName("rate");
    sfm.setColumn("rate");
    sfm.setLabel("˰��");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);
    
    sfm = new SmartFieldMeta();
    sfm.setType(4);
    sfm.setDbType(1);
    sfm.setName("effectdate");
    sfm.setColumn("effectdate");
    sfm.setLabel("��Ч����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);
    
    sfm = new SmartFieldMeta();
    sfm.setType(4);
    sfm.setDbType(1);
    sfm.setName("expirationdate");
    sfm.setColumn("expirationdate");
    sfm.setLabel("ʧЧ����");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(8);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vdoname");
    sfm.setColumn("vdoname");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vtranscustcode");
    sfm.setColumn("vtranscustcode");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vtranscustname");
    sfm.setColumn("vtranscustname");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vvhcltypecode");
    sfm.setColumn("vvhcltypecode");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vvhcltypename");
    sfm.setColumn("vvhcltypename");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vclasscode");
    sfm.setColumn("vclasscode");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vclassname");
    sfm.setColumn("vclassname");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vsendtypecode");
    sfm.setColumn("vsendtypecode");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vsendtypename");
    sfm.setColumn("vsendtypename");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vinvclasscode");
    sfm.setColumn("vinvclasscode");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vinvclassname");
    sfm.setColumn("vinvclassname");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vinvcode");
    sfm.setColumn("vinvcode");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("vinvname");
    sfm.setColumn("vinvname");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("pkcorp");
    sfm.setColumn("pkcorp");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("fromarea");
    sfm.setColumn("fromarea");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("toarea");
    sfm.setColumn("toarea");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("packsort");
    sfm.setColumn("packsort");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(false);
    hmColumn.put(sfm.getName(), sfm);

    sfm = new SmartFieldMeta();
    sfm.setType(3);
    sfm.setDbType(1);
    sfm.setName("ts");
    sfm.setColumn("ts");
    sfm.setLabel("");
    sfm.setColumnDef(null);
    sfm.setAllowNull(false);
    sfm.setPrecision(0);
    sfm.setLength(20);
    sfm.setPersistence(true);
    hmColumn.put(sfm.getName(), sfm);
    setColumns(hmColumn);
  }
}