package nc.ui.so.so002;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.com.google.gson.Gson;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.bd.CorpBO_Client;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
@SuppressWarnings("all")
public class BaoyinSaleInvoiceUI extends SaleInvoiceUI implements SaleInvoiceInterface{

	private boolean ismodify = false;
	  
	public void onButtonClicked(ButtonObject bo) {
		if(bo.getName().equals("库存销售出库单")||bo.getName().equals("修改")){
			this.getButtonObjectByCode("签呈抵扣").setEnabled(true);
		}else{
			this.getButtonObjectByCode("签呈抵扣").setEnabled(false);
		}
		if(nc.ui.so.so002.BaoyinSaleInvoiceAdminUI.strBatchModify.equals(bo.getName()))
		{
			
			SaleinvoiceVO tabelVo = (SaleinvoiceVO)getVO();
			SaleinvoiceVO selectedVo = (SaleinvoiceVO)getVO(true);
			SaleinvoiceBVO[] bodySelectedVos = selectedVo.getItemVOs();
			if(bodySelectedVos == null || bodySelectedVos.length != 1 )
			{
				showErrorMessage("请选择表体[1]行");
				return;
			}
			boolean b = EditableCtrl.isSoOrderPriceCtrl(getPKs(tabelVo.getItemVOs()));
			if(b == false)
			{
		        UFDouble originalcurtaxprice = bodySelectedVos[0].getNoriginalcurtaxprice();
		        setSamePriceInfoFromMyself(bodySelectedVos[0]);
			}else
			{
				showErrorMessage("上游订单价格已锁定");
			}
		} else if ("尾差调整".equals(bo.getName())) {
	        onboRevise(); 
	    } else if("签呈抵扣".equals(bo.getName())){
	    	List <Map> mapresult = new ArrayList<Map>();
	    	List list = new ArrayList();
	    	list = this.onQueryQC();
	    	BankBatEditDlg bank = new BankBatEditDlg();
	    	bank.setReturnValue(list);
	    	bank.showModal();
			mapresult = bank.connEtoC1();
			onQCassign(mapresult);
	    }else{
        	 super.onButtonClicked(bo);
	         }
		}
	/**
	 * 签呈分配 
	 * @return
	 */
	public void onQCassign(List list){
		if(list.size()>0){
			SaleinvoiceVO vos = (SaleinvoiceVO) this.getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
			SaleinvoiceBVO[] bvo = vos.getItemVOs();
			SaleinvoiceBVO[] bvos = new SaleinvoiceBVO[bvo.length];
			Map map = new HashMap();
			UFDouble sjdkje = new UFDouble(0);
			for(int a =0;a<list.size();a++){
				map = (Map) list.get(a);
				//实际抵扣金额
				String sjdk = map.get("sjdkje")==null?"0": map.get("sjdkje").toString();//30
				sjdkje = new UFDouble(sjdk).add(sjdkje);
				String qc = map.get("qcje")==null?"0":map.get("qcje").toString();
				String sj = map.get("sjdkje")==null?"0":map.get("sjdkje").toString();
				UFDouble qc1 = new UFDouble(qc);
				UFDouble sj1 = new UFDouble(sj);
				if(qc1.sub(sj1).doubleValue()<0){
					return;
				}
			}
			
			if(bvo.length>1){
				for(int i =0;i<bvo.length;i++){
					//存放CVM实际抵扣金额
					getBillCardPanel().setBodyValueAt(list.toString(), i, "b_cjje1");
					bvo[i].setB_cjje1(String.valueOf(list.toString()));
					UFDouble norsummny = bvo[i].getNoriginalcursummny();
					if(sjdkje.sub(norsummny).doubleValue()>=0){
						getBillCardPanel().setBodyValueAt(String.valueOf(norsummny), i, "b_cjje3");
						bvo[i].setB_cjje3(String.valueOf(norsummny));
						sjdkje = sjdkje.sub(norsummny);
					}else{
						if(sjdkje.sub(norsummny).doubleValue()<0){
							getBillCardPanel().setBodyValueAt(String.valueOf(sjdkje), i, "b_cjje3");
							bvo[i].setB_cjje3(String.valueOf(String.valueOf(sjdkje)));
							sjdkje = sjdkje.sub(sjdkje);
					}
				}	
			}
			}else{
				for(int i =0;i<bvo.length;i++){
					getBillCardPanel().setBodyValueAt(list.toString(), i, "b_cjje1");
					bvo[i].setB_cjje1(String.valueOf(list.toString()));
					UFDouble norsummny = bvo[i].getNoriginalcursummny();
					if(sjdkje.sub(norsummny).doubleValue()>=0){
						MessageDialog.showErrorDlg(this, "错误提示","所选择签呈金额不能大于发票总金额!");
					}else{
						getBillCardPanel().setBodyValueAt(String.valueOf(sjdkje), i, "b_cjje3");
						bvo[i].setB_cjje3(String.valueOf(String.valueOf(sjdkje)));
					}
				}
			}
		}
	}
	/**
	 * 获取抵扣签呈
	 * @return
	 */
	@SuppressWarnings("all")
	public  List onQueryQC() {
		SaleinvoiceVO vos = (SaleinvoiceVO) this.getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
		String fstatus = this.getBillCardPanel().getHeadItem("fstatus").getValueObject().toString();
		if(fstatus.equals(2)){
			
		}
		String pk_corp = this.getBillCardPanel().getHeadItem("pk_corp").getValueObject().toString();
		String custcode = (String) vos.getParentVO().getAttributeValue("creceiptcorpid");
		String PrimaryKey = null;
		try {
			PrimaryKey = vos.getParentVO().getPrimaryKey();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		String result = saleservice(custcode, pk_corp, PrimaryKey);
		JSONArray jsonarray = new JSONArray();
		jsonarray = new JSONArray().fromObject(result);
		JSONObject jsonObject = new JSONObject();
		Map mls = new HashMap();
		List listMap = new ArrayList();
		for (int j = 0; j < jsonarray.size(); j++) {
			try {
				jsonObject = jsonarray.getJSONObject(j);
				Iterator iterator = jsonObject.keys();
				String key = null;
				String value = null;
				while (iterator.hasNext()) {
					key = (String) iterator.next();
					value = jsonObject.getString(key);
					if (key.equals("bodylist")) {
						JSONArray jsonArray = jsonObject.getJSONArray(key);
						for (int i = 0; i < jsonArray.size(); i++) {
							String key1 = new String();
							String value1 = new String();
							JSONObject jsonOb = jsonArray.getJSONObject(i);
							Map map1 = new HashMap();
							Iterator iter = jsonOb.keys();
							while (iter.hasNext()) {
								key1 = (String) iter.next();
								value1 = jsonOb.getString(key1);
								map1.put(key1, value1);
							}
							listMap.add(map1);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listMap;	
	}
	 /**
	   * 调用CVM销售发票接口来获取抵扣金额
	   * @param custid
	   * @param pk_corp
	   * @return
	   */
	@SuppressWarnings("all")
	  private String saleservice(String custid,String pk_corp,String PrimaryKey){ 
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String resutl =null;
			StringBuffer sql =new StringBuffer();
			//查询客商信息
			sql.append(" select aa.custcode,corp.unitcode ") 
			.append("   from bd_cubasdoc aa ") 
			.append("   left join bd_cumandoc bb ") 
			.append("     on aa.pk_cubasdoc = bb.pk_cubasdoc ") 
			.append("    left join bd_corp corp  ") 
			.append("    on corp.pk_corp = bb.pk_corp ") 
			.append("  where bb.pk_cumandoc = '"+custid+"' ") 
			.append("    and bb.pk_corp = '"+pk_corp+"' ") 
			.append("    and nvl(bb.dr,0)= 0 ");
			List custList;
			//接收cvm抵扣总金额
			String dkje = null;
			Map custMap = new HashMap();
			try {
				custList = (List) bs.executeQuery(sql.toString(),new MapListProcessor());
				if(custList.size()>0){
					custMap = (Map) custList.get(0);
				}
				Map JsonMap = new HashMap();
				JsonMap.put("username", "baosteel");
				JsonMap.put("password", "123456");
				JsonMap.put("corp", (String)custMap.get("unitcode"));
				JsonMap.put("kscode", (String)custMap.get("custcode"));
				JsonMap.put("ncpk", PrimaryKey);
				Gson gson = new Gson();
				Object ss = gson.toJson(JsonMap);
				StringBuffer jsonsb = new StringBuffer();
				jsonsb.append("[").append(ss).append("]");
				URL url;
				try {
					//调用CVM HTTP接口
					url = new URL("http://10.70.26.23/cvm/CVMService/NC003");
					//CVM返回值
					 resutl = this.httpconnect(url, jsonsb);
				}catch (MalformedURLException e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
				}catch (Exception e){
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库查询异常'"+e.getMessage()+"'\"}]";
			}
			return resutl;
		}
	 /**
	   * 反馈销售发票实际抵扣项金额现实方法
	   * @param vo
	   * @return
	   */
	@SuppressWarnings("all")
	public String conSaleSerivce(SaleinvoiceVO saleinvoice) {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		SaleVO hvo = (SaleVO) saleinvoice.getParentVO();
		SaleinvoiceBVO[] bvo = (SaleinvoiceBVO[]) saleinvoice.getChildrenVO();
		String corp  = hvo.getPk_corp();
		String csaleid = hvo.getCsaleid();
		UFDouble cjje3 = new UFDouble(0);//实际抵扣金额
		String CVMjsonstr = null;  //cvm签呈json
		//获取表体数据
		for(int i =0;i<bvo.length;i++){
			CVMjsonstr = bvo[i].getB_cjje1();
			//cvm签呈金额
			UFDouble cvmdkje =new UFDouble(bvo[i].getB_cjje3()==null?"0":bvo[i].getB_cjje3());
			cjje3 = cjje3.add(cvmdkje);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("  select corp.unitcode,cubs.custcode,sale.vreceiptcode ") 
		  .append("        from so_saleinvoice sale") 
		  .append("        left  join bd_corp corp   ") 
		  .append("         on sale.pk_corp = corp.pk_corp   ") 
		  .append("        left join bd_cumandoc cuman    ") 
		  .append("         on cuman.pk_cumandoc = sale.creceiptcorpid   ") 
		  .append("         left join bd_cubasdoc cubs    ") 
		  .append("          on cubs.pk_cubasdoc =cuman.pk_cubasdoc      ") 
		  .append("       where sale.csaleid = '"+csaleid+"' and sale.pk_corp ='"+corp+"' and nvl(sale.dr,0) = 0 ; "); 
		Map JsonMap = new HashMap();
		try {
			List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			if(list.size()>0){
				JsonMap = (HashMap) list.get(0);
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
		String mess = null;
		String gongsi = (String) JsonMap.get("unitcode");
		String keshang =(String) JsonMap.get("custcode");
		String vreceiptcode = (String) JsonMap.get("vreceiptcode");
		
		JSONObject jsonObj = new JSONObject();
		Map body = new HashMap();
		JSONArray  reqjsonArr = new JSONArray();
		JSONArray  jsonArr = new JSONArray();
		JSONArray bodyArr = new JSONArray();
		jsonArr = JSONArray.fromObject(CVMjsonstr);
		Map map = new HashMap();
		
		for(int a = 0;a<jsonArr.size();a++){
			map = (Map) jsonArr.get(a);
			//签呈编码
			String qcbh = map.get("qcbm")==null?"":map.get("qcbm").toString();
			//签呈金额
			String qcje = map.get("sjdkje")==null?"0":map.get("sjdkje").toString();
			
			if(cjje3.sub(new UFDouble(qcje)).doubleValue()>=0){
				jsonObj.put("username", "baosteel");
				jsonObj.put("password", "123456");
				jsonObj.put("gscode", gongsi);
				jsonObj.put("kscode", keshang);
				jsonObj.put("ncpk", csaleid);
				jsonObj.put("ncbh", vreceiptcode);
				body.put("qcbh", qcbh);
				body.put("dkje", qcje);
				bodyArr.add(body);
				cjje3 = cjje3.sub(new UFDouble(qcje));
			}
		}
		jsonObj.put("bodylist", bodyArr);
		reqjsonArr.add(jsonObj);
		//调用CVM接口反馈实际折扣金额
		URL url;
		try {
			url = new URL("http://10.70.26.23/cvm/CVMService/NC004");
			mess = this.httpconnect(url, reqjsonArr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}catch (Exception e) {
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}
		
		return mess;
	}
	  /**
       * 调用cvm接口
       * @param url
       * @param json
       * @return
       * @throws Exception
       */
				
	  private String httpconnect(URL url,Object json) throws Exception {
	        //创建HTTP链接
	        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    
	        //设置请求的方法类型
	        httpURLConnection.setRequestMethod("POST");
	        
	        //设置请求的内容类型
	        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
	        
	        //设置发送数据
	        httpURLConnection.setDoOutput(true);
	        //设置接受数据
	        httpURLConnection.setDoInput(true);
	        
	        //发送数据,使用输出流
	        OutputStream outputStream = httpURLConnection.getOutputStream();
	        //发送的soap协议的数据
	        String content = "jsonData="+URLEncoder.encode(json.toString(), "UTF-8");
	        //发送数据
	        outputStream.write(content.getBytes());
	    
	        //接收数据
	        InputStream inputStream = httpURLConnection.getInputStream();
	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
	        StringBuffer buffer = new StringBuffer();  
	        String line = "";  
	        while ((line = in.readLine()) != null){  
	          buffer.append(line);  
	        }  
	        String reustle =null;
	        reustle = buffer.toString();
	        in.close();
	        
	        //String str ="[{\"success\":true,\"bodylist\":[{\"qcbh\":\"A\",\"kdkje\":\"5000\"},{\"qcbh\":\"B\",\"kdkje\":\"350\"}],\"message\":\"获取成功\",\"kscode\":\"210303555\",\"status\":\"success\",\"gscode\":\"10301\"}]";
	        //解析CVM返回JSON数据
	        return reustle;
	  }
		/**
		 * 解析json
		 * @param jsonStr
		 * @return
		 */
		private String Analysis(String jsonStr){
		    Gson gson = new Gson();
	        Map<String, Object> map = new HashMap<String, Object>();
	        map = gson.fromJson(jsonStr, map.getClass());
	        String status = (String) map.get("status");
			String dkje = null;
			if(status.equals("success")){
				dkje =  map.get("dkje").toString()==null?"0": map.get("dkje").toString();
				return dkje;
			}else{
				dkje = "0";
				return dkje;
			}
		}
	  /**
	   * 查询客商编码
	   * @param UserCode
	   * @return
	   */
	  private String getCode(String UserCode){
		    IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        StringBuffer sb = new StringBuffer();
	        //部门编码
	        String pk_dept = null;
	        //根据登录code查询登录用户名
	        sb.append(" select user_name from sm_user where user_code='"+UserCode+"' and nvl(dr,0)=0  ");
	        List userList = null;
			try {
				userList = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				  HashMap<String, String> userMap = new HashMap<String, String>();
			        if(userList.size()>0){
				      	  userMap = (HashMap<String, String>) userList.get(0);
				      	  StringBuffer ss = new StringBuffer();
				      	  //根据登录用户名查询所属部门
				      	  ss.append("select pk_deptdoc from bd_psndoc where psnname = '"+userMap.get("user_name")+"' and nvl(dr,0)=0");
				      	  List psnList  = (List) bs.executeQuery(ss.toString(), new MapListProcessor());
				      	  HashMap<String, String> psnMap = new HashMap<String, String>();
				      	  if(psnList.size()>0){
				      		  psnMap = (HashMap<String, String>) psnList.get(0);
				      		  pk_dept = psnMap.get("pk_deptdoc")==null?"":psnMap.get("pk_deptdoc");
				      	  }
			         }
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			if(userList.size()>0){
				return pk_dept;
			}else{
				return null;
			}
		}
	/**
	 * 设置相同的价格
	 * */
	private void setSamePriceInfoFromMyself(SaleinvoiceBVO bvo){
        String cinventoryid = bvo.getCinventoryid();
        UFDouble originalcurtaxprice = bvo.getNoriginalcurtaxprice();

        SaleinvoiceBVO[] itemVOs = ((SaleinvoiceVO)getVO()).getItemVOs();
        showHintMessage("");
        BillCardPanel panel = (BillCardPanel)getComponent(0);
        BillScrollPane.BillTable table = (BillScrollPane.BillTable)panel.getBillTable();
        int j = 0;
        for (int i = 0; i < itemVOs.length; i++) {
          if (!cinventoryid.equals(itemVOs[i].getCinventoryid()))
            continue;
          if (j == 0) j++;
          table.setRowSelectionInterval(i, i);
          panel.getBillTable().scrollRectToVisible(table.getCellRect(i, 11, true));
          getBillCardTools().setBodyValue(originalcurtaxprice, i, "noriginalcurtaxprice");
          BillItem bi = getBillCardTools().getBillData().getBillModel().getItemByKey("noriginalcurtaxprice");
          afterNumberEdit(new BillEditEvent(bi, originalcurtaxprice, "noriginalcurtaxprice", i, 1));
          int status = panel.getBillModel().getRowState(i);
          if (status == 0) {
            panel.getBillModel().setRowState(i, 2);
          }

        }

        if (getParentCorpCode().equals("10395")) {
          UFDouble ntaxrate = bvo.getNtaxrate() == null ? new UFDouble(0) : bvo.getNtaxrate();
          UFDouble nitemdiscountrate = bvo.getNitemdiscountrate() == null ? new UFDouble(0) : bvo.getNitemdiscountrate();
          UFDouble httsbl = bvo.getHttsbl() == null ? new UFDouble(0) : bvo.getHttsbl();
          UFDouble sjtsje = bvo.getSjtsje() == null ? new UFDouble(0) : bvo.getSjtsje();
          for (int i = 0; i < itemVOs.length; i++) {
            if (!cinventoryid.equals(itemVOs[i].getCinventoryid()))
              continue;
            if (j == 0) j++;
            table.setRowSelectionInterval(i, i);
            panel.getBillTable().scrollRectToVisible(table.getCellRect(i, 11, true));

            getBillCardTools().setBodyValue(ntaxrate, i, "ntaxrate");
            BillItem bi = getBillCardTools().getBillData().getBillModel().getItemByKey("ntaxrate");
            afterNumberEdit(new BillEditEvent(bi, ntaxrate, "ntaxrate", i, 1));

            getBillCardTools().setBodyValue(nitemdiscountrate, i, "nitemdiscountrate");
            bi = getBillCardTools().getBillData().getBillModel().getItemByKey("nitemdiscountrate");
            afterNumberEdit(new BillEditEvent(bi, nitemdiscountrate, "nitemdiscountrate", i, 1));

            getBillCardTools().setBodyValue(httsbl, i, "httsbl");
            bi = getBillCardTools().getBillData().getBillModel().getItemByKey("httsbl");
            afterNumberEdit(new BillEditEvent(bi, httsbl, "httsbl", i, 1));

            getBillCardTools().setBodyValue(sjtsje, i, "sjtsje");
            bi = getBillCardTools().getBillData().getBillModel().getItemByKey("sjtsje");
            afterEdit(new BillEditEvent(bi, sjtsje.toString(), "sjtsje", i, 1));
            afterEdit(new BillEditEvent(bi, ntaxrate, "ntaxrate", i, 1));

            UFDouble num = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "nnumber") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "nnumber")));

            UFDouble noriginalcurtaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcurtaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcurtaxprice")));

            UFDouble ntaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ntaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ntaxprice")));

            UFDouble newamount2 = num.multiply(noriginalcurtaxprice).multiply(new UFDouble(100).sub(nitemdiscountrate).div(new UFDouble(100)));
            getBillCardPanel().setBodyValueAt(newamount2.setScale(2, 4), i, "noriginalcurdiscountmny");

            UFDouble newamount6 = num.multiply(ntaxprice).multiply(new UFDouble(100).sub(nitemdiscountrate).div(new UFDouble(100)));
            getBillCardPanel().setBodyValueAt(newamount6.setScale(2, 4), i, "ndiscountmny");

            UFDouble ndiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ndiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ndiscountmny")));

            getBillCardPanel().setBodyValueAt(ndiscountmny.add(sjtsje), i, "ztsje");

            int status = panel.getBillModel().getRowState(i);
            if (status == 0) {
              panel.getBillModel().setRowState(i, 2);
            }
          }
        }

        if (j != 0)
          showHintMessage("批改完成");
	}
	@Override
	  public void onNew()
	  {
	    super.onNew();
	    onNew1();
	    setPriceItemEditState();
	    this.ismodify = true;
	  }
	  @Override
	  public void onModify()
	  {
	    super.onModify();
	    onModify1();
	    setPriceItemEditState();
	    this.ismodify = true;
	  }

	  private void setPriceItemEditState() {
	    EditableCtrl.setPriceItemEditState(getBillCardPanel().getBillModel(), Boolean.valueOf(isSoOrderPriceCtrl()));
	  }

	  private boolean isSoOrderPriceCtrl()
	  {
	    SaleinvoiceBVO[] itemVOs = (SaleinvoiceBVO[])getVO().getChildrenVO();
	    return EditableCtrl.isSoOrderPriceCtrl(getPKs(itemVOs));
	  }

	  private String getPKs(SaleinvoiceBVO[] bvos)
	  {
	    StringBuffer pks = new StringBuffer();
	    for (SaleinvoiceBVO moVO : bvos) {
	      pks.append("'" + moVO.getPrimaryKey() + "',");
	    }
	    if (pks.length() > 0)
	    {
	      pks.deleteCharAt(pks.length() - 1);
	    }
	    return pks.toString();
	  }

	  public void onModify1()
	  {
	  }

	  public void onNew1()
	  {
	  }

	  @Override
	  public void afterEdit(BillEditEvent e)
	  {
	    this.ismodify = true;
	    super.afterEdit(e);
	    int row = e.getRow();

	    if ((getParentCorpCode().equals("10395")) && (row >= 0))
	    {
	      UFDouble ndiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ndiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ndiscountmny")));

	      UFDouble sjtsje = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "sjtsje") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "sjtsje")));

	      UFDouble ztsje = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ztsje") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ztsje")));
	      if (ztsje.compareTo(ndiscountmny.add(sjtsje)) != 0) {
	        getBillCardPanel().setBodyValueAt(ndiscountmny.add(sjtsje), row, "ztsje");
	        if (getBillCardPanel().getBillModel().getRowState(row) != 1) {
	          getBillCardPanel().getBillModel().setRowState(row, 2);
	        }

	      }

	      UFDouble httsbl = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "httsbl") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "httsbl")));

	      UFDouble num = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "nnumber") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "nnumber")));

	      UFDouble httssl = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "httssl") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "httssl")));
	      if (httssl.compareTo(num.multiply(httsbl.div(new UFDouble(100)))) != 0) {
	        getBillCardPanel().setBodyValueAt(num.multiply(httsbl.div(new UFDouble(100))), row, "httssl");
	        if (getBillCardPanel().getBillModel().getRowState(row) != 1) {
	          getBillCardPanel().getBillModel().setRowState(row, 2);
	        }

	      }

	      UFDouble noriginalcurtaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice")));

	      UFDouble ntaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ntaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ntaxprice")));

	      UFDouble ntaxrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ntaxrate") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ntaxrate")));

	      UFDouble noriginalcurtaxmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxmny")));

	      UFDouble ntaxmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ntaxmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ntaxmny")));

	      UFDouble nitemdiscountrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate")));

	      UFDouble noriginalcurdiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurdiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurdiscountmny")));
	      String b_cjje2 = getBillCardPanel().getBodyValueAt(row, "b_cjje2")==null?"0": (String) getBillCardPanel().getBodyValueAt(row, "b_cjje2");
	      
	      UFDouble newamount = num.multiply(noriginalcurtaxprice).multiply(nitemdiscountrate).div(new UFDouble(100)).sub(sjtsje).sub(new UFDouble(b_cjje2));
	      getBillCardPanel().setBodyValueAt(newamount, row, "noriginalcursummny");

	      UFDouble newamount5 = num.multiply(ntaxprice).multiply(nitemdiscountrate).div(new UFDouble(100)).sub(sjtsje).sub(new UFDouble(b_cjje2));
	      getBillCardPanel().setBodyValueAt(newamount5, row, "nsummny");
	    }
	  }
	
	@Override
	public void onSave() {
	    if ((getParentCorpCode().equals("10395")) && (this.ismodify))
	    {
	      onboRevise();
	      return;
	    }
	    super.onSave();
	  }

	
	/**
  	 * @功能:返回公司的上级公司编码
  	 * @author ：cm
  	 * @2012/9/5
  	 * 
  	 * @since v50
  	 */
  	public String getParentCorpCode() {

  		String ParentCorp = new String();
  		String key = ClientEnvironment.getInstance().getCorporation()
  				.getFathercorp();
  		try {
  			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
  			ParentCorp = corpVO.getUnitcode();
  		} catch (BusinessException e) {
  			e.printStackTrace();
  		}
  		return ParentCorp;
  	}
	
	protected void execdata(int row ,String itemname){
		nc.ui.so.so001.panel.bom.BillTools.calcEditFun(getBillCardPanel().getHeadItem("dbilldate").getValue(), this.BD505, true,"noriginalcursummny", row, getBillCardPanel());
		UFDouble dbTemp = computeViaPrice(row);
	    if (dbTemp != null) {
	      getBillCardPanel().getBillModel().setValueAt(dbTemp,row, "norgviaprice");
	    }

	    dbTemp = computeViaPriceTax(row);
	    if (dbTemp != null) {
	      getBillCardPanel().getBillModel().setValueAt(dbTemp, row, "norgviapricetax");
	    }

	    getBillCardPanel().setHeadItem("ntotalsummny", calcurateTotal("noriginalcursummny"));

	    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
	    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "noriginalcursummny"), row, "nsubsummny");

	    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nsummny"),row, "nsubcursummny");
	}
	
	protected UFDouble computeViaPrice(int iRow)
	  {
	    Object obj1 = getBillCardPanel().getBodyValueAt(iRow, "noriginalcurtaxprice");

	    Object obj2 = getBillCardPanel().getBodyValueAt(iRow, "ntaxrate");

	    Object obj3 = getBillCardPanel().getBodyValueAt(iRow, "nnumber");

	    Object obj4 = getBillCardPanel().getBodyValueAt(iRow, "npacknumber");

	    if ((obj1 != null) && (obj2 != null) && (obj3 != null) && (obj4 != null)) {
	      UFDouble dTaxRate = ((UFDouble)obj2).div(100.0D).add(1.0D);

	      return ((UFDouble)obj1).div(dTaxRate).multiply(((UFDouble)obj3).div((UFDouble)obj4));
	    }

	    return null;
	  }
	  
	  protected UFDouble computeViaPriceTax(int iRow)
	  {
	    Object obj1 = getBillCardPanel().getBodyValueAt(iRow, "noriginalcurtaxprice");

	    Object obj3 = getBillCardPanel().getBodyValueAt(iRow, "nnumber");

	    Object obj4 = getBillCardPanel().getBodyValueAt(iRow, "npacknumber");

	    if ((obj1 != null) && (obj3 != null) && (obj4 != null))
	    {
	      return ((UFDouble)obj1).multiply(((UFDouble)obj3).div((UFDouble)obj4));
	    }

	    return null;
	  }
	  private void onboRevise()
	  {
	    if (getParentCorpCode().equals("10395")) {
	      showHintMessage("尾差调整");
	      int rowcount = getBillCardPanel().getBillTable().getRowCount();
	      Map map = new HashMap();
	      for (int i = 0; i < rowcount; i++)
	      {
	        UFDouble nitemdiscountrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "nitemdiscountrate") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "nitemdiscountrate")));

	        UFDouble noriginalcurdiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcurdiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcurdiscountmny")));

	        UFDouble ndiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ndiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ndiscountmny")));
	        execdata(i, "nitemdiscountrate");
	        getBillCardPanel().setBodyValueAt(nitemdiscountrate, i, "nitemdiscountrate");
	        getBillCardPanel().setBodyValueAt(noriginalcurdiscountmny, i, "noriginalcurdiscountmny");
	        getBillCardPanel().setBodyValueAt(ndiscountmny, i, "ndiscountmny");
	        getBillCardPanel().updateUI();

	        String crowno = String.valueOf(getBillCardPanel().getBodyValueAt(i, "crowno"));

	        String invcode = String.valueOf(getBillCardPanel().getBodyValueAt(i, "cinventorycode"));

	        UFDouble num = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "nnumber") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "nnumber")));

	        UFDouble noriginalcurtaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcurtaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcurtaxprice")));

	        UFDouble ntaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ntaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ntaxprice")));

	        UFDouble ntaxrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ntaxrate") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ntaxrate")));

	        UFDouble noriginalcurtaxmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcurtaxmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcurtaxmny")));

	        UFDouble ntaxmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ntaxmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ntaxmny")));

	        UFDouble noriginalcurmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcurmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcurmny")));

	        UFDouble nmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "nmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "nmny")));

	        UFDouble noriginalcursummny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcursummny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcursummny")));

	        UFDouble nsummny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "nsummny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "nsummny")));

	        nitemdiscountrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "nitemdiscountrate") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "nitemdiscountrate")));

	        noriginalcurdiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "noriginalcurdiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "noriginalcurdiscountmny")));

	        ndiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ndiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ndiscountmny")));

	        UFDouble sjtsje = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "sjtsje") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "sjtsje")));

	        //pjj
		    String bcjje = getBillCardPanel().getBodyValueAt(i, "b_cjje3")==null?"0":getBillCardPanel().getBodyValueAt(i, "b_cjje3").toString();
		    
	        UFDouble httsbl = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "httsbl") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "httsbl")));

	        UFDouble httssl = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "httssl") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "httssl")));

	        UFDouble ztsje = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(i, "ztsje") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(i, "ztsje")));
	        
	        if (ztsje.compareTo(ndiscountmny.add(sjtsje)) != 0) {
	          getBillCardPanel().setBodyValueAt(ndiscountmny.add(sjtsje), i, "ztsje");
	          if (getBillCardPanel().getBillModel().getRowState(i) != 1) {
	            getBillCardPanel().getBillModel().setRowState(i, 2);
	          }
	        }
	        if ((invcode == null) || ("".equals(invcode)) || (invcode.length() < 4))
	        {
	          continue;
	        }
	        if (map.containsKey(invcode))
	        {
	          //pjj
	          UFDouble newamount = num.multiply(noriginalcurtaxprice).multiply(nitemdiscountrate).div(new UFDouble(100)).sub(sjtsje).sub(new UFDouble(bcjje));

	          UFDouble newamount2 = num.multiply(noriginalcurtaxprice).multiply(new UFDouble(100).sub(nitemdiscountrate).div(new UFDouble(100)));

	          UFDouble newamount3 = newamount.multiply(ntaxrate.div(new UFDouble(100))).div(new UFDouble(100).add(ntaxrate).div(new UFDouble(100)));

	          UFDouble newamount4 = newamount.sub(newamount3);
	          //pjj
	          UFDouble newamount5 = num.multiply(ntaxprice).multiply(nitemdiscountrate).div(new UFDouble(100)).sub(sjtsje).sub(new UFDouble(bcjje));

	          UFDouble newamount6 = num.multiply(ntaxprice).multiply(new UFDouble(100).sub(nitemdiscountrate).div(new UFDouble(100)));

	          UFDouble newamount7 = newamount5.multiply(ntaxrate.div(new UFDouble(100))).div(new UFDouble(100).add(ntaxrate).div(new UFDouble(100)));

	          UFDouble newamount8 = newamount5.sub(newamount7);

	          UFDouble newamount9 = num.multiply(httsbl).div(new UFDouble(100));

	          UFDouble[] amounts = (UFDouble[])map.get(invcode);
	          amounts[0] = amounts[0].add(noriginalcursummny);
	          amounts[1] = amounts[1].add(newamount);
	          amounts[2] = amounts[2].add(noriginalcurdiscountmny);
	          amounts[3] = amounts[3].add(newamount2);
	          amounts[4] = new UFDouble(i);
	          amounts[5] = amounts[5].add(noriginalcurtaxmny);
	          amounts[6] = amounts[6].add(newamount3);
	          amounts[7] = amounts[7].add(noriginalcurmny);
	          amounts[8] = amounts[8].add(newamount4);
	          amounts[9] = amounts[9].add(nsummny);
	          amounts[10] = amounts[10].add(newamount5);
	          amounts[11] = amounts[11].add(ndiscountmny);
	          amounts[12] = amounts[12].add(newamount6);
	          amounts[13] = amounts[13].add(ntaxmny);
	          amounts[14] = amounts[14].add(newamount7);
	          amounts[15] = amounts[15].add(nmny);
	          amounts[16] = amounts[16].add(newamount8);
	          amounts[17] = amounts[17].add(httssl);
	          amounts[18] = amounts[18].add(newamount9);
	          amounts[19] = amounts[19].add(num);
	          map.put(invcode, amounts);
	        }
	        else {
	          //pjj
	          UFDouble newamount = num.multiply(noriginalcurtaxprice).multiply(nitemdiscountrate).div(new UFDouble(100)).sub(sjtsje).sub(new UFDouble(bcjje));

	          UFDouble newamount2 = num.multiply(noriginalcurtaxprice).multiply(new UFDouble(100).sub(nitemdiscountrate).div(new UFDouble(100)));

	          UFDouble newamount3 = newamount.multiply(ntaxrate.div(new UFDouble(100))).div(new UFDouble(100).add(ntaxrate).div(new UFDouble(100)));

	          UFDouble newamount4 = newamount.sub(newamount3);
	          //pjj
	          UFDouble newamount5 = num.multiply(ntaxprice).multiply(nitemdiscountrate).div(new UFDouble(100)).sub(sjtsje).sub(new UFDouble(bcjje));

	          UFDouble newamount6 = num.multiply(ntaxprice).multiply(new UFDouble(100).sub(nitemdiscountrate).div(new UFDouble(100)));

	          UFDouble newamount7 = newamount5.multiply(ntaxrate.div(new UFDouble(100))).div(new UFDouble(100).add(ntaxrate).div(new UFDouble(100)));

	          UFDouble newamount8 = newamount5.sub(newamount7);

	          UFDouble newamount9 = num.multiply(httsbl).div(new UFDouble(100));

	          UFDouble[] amounts = new UFDouble[20];
	          amounts[0] = noriginalcursummny;
	          amounts[1] = newamount;
	          amounts[2] = noriginalcurdiscountmny;
	          amounts[3] = newamount2;
	          amounts[4] = new UFDouble(i);
	          amounts[5] = noriginalcurtaxmny;
	          amounts[6] = newamount3;
	          amounts[7] = noriginalcurmny;
	          amounts[8] = newamount4;
	          amounts[9] = nsummny;
	          amounts[10] = newamount5;
	          amounts[11] = ndiscountmny;
	          amounts[12] = newamount6;
	          amounts[13] = ntaxmny;
	          amounts[14] = newamount7;
	          amounts[15] = nmny;
	          amounts[16] = newamount8;
	          amounts[17] = httssl;
	          amounts[18] = newamount9;
	          amounts[19] = num;
	          map.put(invcode, amounts);
	        }
	      }

	      Iterator it = map.values().iterator();
	      while (it.hasNext()) {
	        UFDouble[] amounts = (UFDouble[])it.next();
	        int row = amounts[4].intValue();
	        UFDouble diff = amounts[1].sub(amounts[0]);

	        UFDouble noriginalcursummny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcursummny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcursummny")));
	        getBillCardPanel().setBodyValueAt(noriginalcursummny.add(diff), row, "noriginalcursummny");

	        UFDouble nitemdiscountrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate")));

	        UFDouble noriginalcurdiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurdiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurdiscountmny")));

	        UFDouble noriginalcurtaxmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxmny")));

	        UFDouble noriginalcurmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurmny")));

	        UFDouble noriginalcurtaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice")));

	        UFDouble ntaxprice = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ntaxprice") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ntaxprice")));

	        UFDouble ntaxrate = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ntaxrate") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ntaxrate")));

	        UFDouble ndiscountmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ndiscountmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ndiscountmny")));

	        UFDouble ntaxmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "ntaxmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "ntaxmny")));

	        UFDouble nmny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "nmny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "nmny")));

	        UFDouble httssl = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "httssl") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "httssl")));

	        UFDouble diff5 = amounts[10].sub(amounts[9]);

	        UFDouble nsummny = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "nsummny") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "nsummny")));
	        getBillCardPanel().setBodyValueAt(nsummny.add(diff5), row, "nsummny");

	        execdata(row, "noriginalcursummny");

	        getBillCardPanel().setBodyValueAt(nitemdiscountrate, row, "nitemdiscountrate");

	        UFDouble diff2 = amounts[3].sub(amounts[2]);
	        getBillCardPanel().setBodyValueAt(noriginalcurdiscountmny.add(diff2), row, "noriginalcurdiscountmny");

	        UFDouble tax1 = amounts[1].setScale(2, 4).add(amounts[3].setScale(2, 4)).div(new UFDouble(1).add(ntaxrate.div(100.0D))).multiply(ntaxrate.div(100.0D)).setScale(2, 4);
	        UFDouble tax2 = amounts[3].setScale(2, 4).sub(amounts[3].div(new UFDouble(1).add(ntaxrate.div(100.0D))).setScale(2, 4));
	        amounts[6] = tax1.setScale(2, 4).sub(tax2.setScale(2, 4));
	        UFDouble diff3 = amounts[6].sub(amounts[5]);
	        getBillCardPanel().setBodyValueAt(noriginalcurtaxmny.add(diff3), row, "noriginalcurtaxmny");

	        amounts[8] = amounts[1].sub(amounts[6]);
	        UFDouble diff4 = amounts[8].sub(amounts[7]);
	        getBillCardPanel().setBodyValueAt(noriginalcurmny.add(diff4), row, "noriginalcurmny");
	        
	        UFDouble diff6 = amounts[12].sub(amounts[11]);
	        getBillCardPanel().setBodyValueAt(ndiscountmny.add(diff6), row, "ndiscountmny");

	        tax1 = amounts[10].setScale(2, 4).add(amounts[12].setScale(2, 4)).div(new UFDouble(1).add(ntaxrate.div(100.0D))).multiply(ntaxrate.div(100.0D)).setScale(2, 4);
	        tax2 = amounts[12].setScale(2, 4).sub(amounts[12].div(new UFDouble(1).add(ntaxrate.div(100.0D))).setScale(2, 4));
	        amounts[14] = tax1.setScale(2, 4).sub(tax2.setScale(2, 4));
	        UFDouble diff7 = amounts[14].sub(amounts[13]);
	        getBillCardPanel().setBodyValueAt(ntaxmny.add(diff7), row, "ntaxmny");

	        amounts[16] = amounts[10].sub(amounts[6]);
	        UFDouble diff8 = amounts[16].sub(amounts[15]);
	        getBillCardPanel().setBodyValueAt(nmny.add(diff8), row, "nmny");

	        UFDouble sjtsje = new UFDouble(String.valueOf(getBillCardPanel().getBodyValueAt(row, "sjtsje") == null ? Integer.valueOf(0) : getBillCardPanel().getBodyValueAt(row, "sjtsje")));

	        getBillCardPanel().setBodyValueAt(ndiscountmny.add(diff6).add(sjtsje), row, "ztsje");

	        UFDouble diff9 = amounts[18].sub(amounts[17]);
	        getBillCardPanel().setBodyValueAt(httssl.add(diff9).setScale(0, 4), row, "httssl");
	        if (getBillCardPanel().getBillModel().getRowState(row) != 1) {
	          getBillCardPanel().getBillModel().setRowState(row, 2);
	        }
	        getBillCardPanel().updateUI();
	      }
	      this.ismodify = false;
	      showHintMessage("尾差调整完成");
	    }
	  }
}
