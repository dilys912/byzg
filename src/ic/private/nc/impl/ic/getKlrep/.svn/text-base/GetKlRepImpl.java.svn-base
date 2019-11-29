package nc.impl.ic.getKlrep;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.borland.jdbc.Connection;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.uap.util.JdbcSessionUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.ic.kl.KlVO;
import nc.vo.ic.kl.TeamKlVO;
import nc.vo.ic.pub.ICGenVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class GetKlRepImpl {
	IUAPQueryBS uap =(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	IUifService uif = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
	private String[] m_sKlsdday = new String[6];
	private String m_sLoginDate = "";
	public void getKlRep(String pk_corp) {
		System.out.println("执行开始了:"+new Date());
		m_sLoginDate = new UFDate(new Date()).toString();
		m_sKlsdday[0]="30";
		m_sKlsdday[1]="60";
		m_sKlsdday[2]="90";
		m_sKlsdday[3]="180";
		m_sKlsdday[4]="360";
		m_sKlsdday[5]="361";
		// TODO Auto-generated method stub
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);
			StringBuffer sql = new StringBuffer();
			//第一步、查询出现存量表中结存数量或者是附表中数量不为0的产品的编码
			sql.append(" SELECT distinct bas.invcode ") 
			.append("   FROM ic_onhandnum h ") 
			.append("   left join ic_onhandnum_b b ") 
			.append("     ON h.pk_onhandnum = b.pk_onhandnum ") 
			.append("   left join bd_invbasdoc bas ") 
			.append("     on bas.pk_invbasdoc = h.cinvbasid ") 
			.append("  where h.pk_corp = '"+pk_corp+"' ") 
			.append("    and (nvl(b.nnum,0) <> 0 or nvl(h.nonhandnum,0) <> 0)") 
			.append("    and (substr(bas.invcode, 0, 2) = '21' or ") 
			.append("         substr(bas.invcode, 0, 2) = '22' or ") 
			.append("         substr(bas.invcode, 0, 2) = '23') "); 

			ArrayList list = new ArrayList();
			try {
				list = (ArrayList)uap.executeQuery(sql.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			List<KlVO[]> arraylist = new ArrayList<KlVO[]>();
			KlVO[] cpvo = null;
			if(list.size()>0 && list != null){
				for(int i = 0 ;i<list.size() ;i++){
					Map map = (Map) list.get(i);
					String invcode = map.get("invcode")==null?"":map.get("invcode").toString();
					cpvo = getCpKl(invcode,pk_corp);//第2步：循环遍历出list中每个成品存货编码并得到它的库龄
					arraylist.add(cpvo);
				}
			}
			List<TeamKlVO> listvo = new ArrayList<TeamKlVO>();
			if(arraylist != null && arraylist.size()>0){
				for(int k = 0 ;k<arraylist.size();k++){
					KlVO[] klvos = arraylist.get(k);
					for(int h = 0; h<klvos.length ;h++){
						KlVO klvo = klvos[h];
						TeamKlVO t = new TeamKlVO();
						String corpid = klvo.getPk_corp();//公司
						String invcode = klvo.getCinventorycode();
						String pk_invbasdoc = klvo.getCinventoryid();
						String invname = klvo.getInvname();
						String pk_stordoc = klvo.getCwarehouseclassid();
						String storcode = klvo.getCwarehousecode();
						String storname = klvo.getCwarehousename();
						String custcode = klvo.getCustcode();
						String custname = klvo.getCustname();
						UFDouble xcl = klvo.getXcl();
						UFDouble price = (UFDouble) (klvo.getAttributeValue("nprice")==null?0:klvo.getAttributeValue("nprice"));
						String kl0 = klvo.getAttributeValue("numberinday0")==null?"0":klvo.getAttributeValue("numberinday0").toString();
						String je0 = klvo.getAttributeValue("nmny0000")==null?"0":klvo.getAttributeValue("nmny0000").toString();
						String kl1 = klvo.getAttributeValue("numberinday1")==null?"0":klvo.getAttributeValue("numberinday1").toString();
						String je1 = klvo.getAttributeValue("nmny0001")==null?"0":klvo.getAttributeValue("nmny0001").toString();
						String kl2 = klvo.getAttributeValue("numberinday2")==null?"0":klvo.getAttributeValue("numberinday2").toString();
						String je2 = klvo.getAttributeValue("nmny0002")==null?"0":klvo.getAttributeValue("nmny0002").toString();
						String kl3 = klvo.getAttributeValue("numberinday3")==null?"0":klvo.getAttributeValue("numberinday3").toString();
						String je3 = klvo.getAttributeValue("nmny0003")==null?"0":klvo.getAttributeValue("nmny0003").toString();
						String kl4 = klvo.getAttributeValue("numberinday4")==null?"0":klvo.getAttributeValue("numberinday4").toString();
						String je4 = klvo.getAttributeValue("nmny0004")==null?"0":klvo.getAttributeValue("nmny0004").toString();
						String kl5 = klvo.getAttributeValue("numberinday5")==null?"0":klvo.getAttributeValue("numberinday5").toString();
						String je5 = klvo.getAttributeValue("nmny0005")==null?"0":klvo.getAttributeValue("nmny0005").toString();
						t.setPk_corp(corpid);
						t.setPk_invbasdoc(pk_invbasdoc);
						t.setInvcode(invcode);
						t.setInvname(invname);
						t.setPk_stordoc(pk_stordoc);
						t.setStorcode(storcode);
						t.setStorname(storname);
						t.setDef1(custcode);
						t.setDef2(custname);
						t.setXcl(xcl);
						t.setPrice(price);
						t.setDr(0);
						t.setKl0(new UFDouble(kl0));
						t.setKl1(new UFDouble(kl1));
						t.setKl2(new UFDouble(kl2));
						t.setKl3(new UFDouble(kl3));
						t.setKl4(new UFDouble(kl4));
						t.setKl5(new UFDouble(kl5));
						t.setJe0(new UFDouble(je0));
						t.setJe1(new UFDouble(je1));
						t.setJe2(new UFDouble(je2));
						t.setJe3(new UFDouble(je3));
						t.setJe4(new UFDouble(je4));
						t.setJe5(new UFDouble(je5));
						listvo.add(t);
					}
				}
			}
			if(listvo.size()>0){
				try {
					new BaseDAO().insertVOList(listvo);
					System.out.println("执行结束了:"+new Date());
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	public Map getCustInfo(String pk_corp,String invcode) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.custcode,c.custname,c.pk_cubasdoc from bd_invbasdoc a ") 
		.append(" left join bd_invmandoc b ") 
		.append(" on a.pk_invbasdoc = b.pk_invbasdoc ") 
		.append(" left join bd_cubasdoc c ") 
		.append(" on b.def17 = c.pk_cubasdoc ") 
		.append(" where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0) ") 
		.append(" and b.pk_corp='"+pk_corp+"' and a.invcode='"+invcode+"' "); 
		List list = (List) uap.executeQuery(sql.toString(), new MapListProcessor());
		if(list.size()>0){
			Map map = (Map) list.get(0);
			return map;
		}
		return null;
	}
	//获取成品库龄
	public KlVO[] getCpKl(String invcode,String pk_corp){
		String firstdate = m_sLoginDate.substring(0,7)+"-01";//得到当前月的第一天的日期（年月日）
		String ymdate = new UFDate(firstdate).getDateBefore(1).toString().substring(0,7);//得到firstdate上一个月的年月
		ArrayList list = getOnhandnum(pk_corp,invcode);//第3步：获取物料的现存量
		StringBuffer vfreeStr = new StringBuffer("");
		List vfree = new ArrayList();
		ArrayList alResultData = new ArrayList(); //结果
		HashMap inv = new HashMap();
		HashMap invxcl = new HashMap();
		if(list.size()>0){
			for(int i =0;i<list.size();i++){
				Map map = (Map) list.get(i);
				String vfree1 = map.get("vfree1") == null ? "" : map.get("vfree1").toString();
				vfree.add(vfree1);
				
				if(map.get("cinventoryid")!=null){
					String cwarehouseclassid = map.get("cwarehouseid")==null?"":map.get("cwarehouseid").toString();
					String cinventoryid = map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();
 					String pk_corp1 = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
 					String cvendorid = "";//客商基本档案主键
 					String custcode = "";
 					String custname = "";
 					if(cvendorid != null){
 						try {
							Map custmap = getCustInfo(pk_corp, map.get("invcode")==null?"":map.get("invcode").toString());
							if(custmap != null){
								custcode = custmap.get("custcode")==null?"":custmap.get("custcode").toString();
								custname = custmap.get("custname")==null?"":custmap.get("custname").toString();
								cvendorid = custmap.get("pk_cubasdoc")==null?"":custmap.get("pk_cubasdoc").toString();
							}
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
 					}
 					//现存量中自由项1 
 					String vfree11 = map.get("vfree1")==null?"":map.get("vfree1").toString();
					if(inv.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+vfree11)){
						KlVO vo = (KlVO) inv.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+vfree11);
						UFDouble xcl =  vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
						num = num.add(xcl);
						vo.setAttributeValue("xcl",num);  
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+vfree11, vo);
					}else{
						KlVO pivo = new KlVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",map.get("storcode"));
						pivo.setAttributeValue("cwarehousename",map.get("storname"));
						pivo.setAttributeValue("cinventorycode",map.get("invcode"));
						pivo.setAttributeValue("invname",map.get("invname"));
//						pivo.setAttributeValue("invspec",map.get(""));
//						pivo.setAttributeValue("invtype",map.get(""));
//						pivo.setAttributeValue("cinvbasid",map.get(""));
//						pivo.setAttributeValue("measdocname",map.get(""));  
						pivo.setAttributeValue("ccalbodycode", map.get("bodycode"));
						pivo.setAttributeValue("ccalbodyname", map.get("bodyname"));
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						pivo.setAttributeValue("custcode", custcode);
						pivo.setAttributeValue("cvendorid", cvendorid);
						pivo.setAttributeValue("custname", custname);
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
				//		Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						inv.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString()+vfree11, pivo);
					}
					
					if(invxcl.containsKey(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString())){
						KlVO vo = (KlVO) invxcl.get(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString());
						UFDouble xcl =vo.getAttributeValue("xcl")==null?new UFDouble(0.0):new UFDouble(vo.getAttributeValue("xcl").toString());
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
					//	Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						num = num.add(xcl);
					//	num = num+Integer.parseInt(xcl.toString());
						vo.setAttributeValue("xcl",num);  
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), vo);
					}else{
						KlVO pivo = new KlVO();
						pivo.setPk_corp(map.get("pk_corp").toString());
						pivo.setAttributeValue("cwarehouseclassid", map.get("cwarehouseid"));
						pivo.setAttributeValue("cwarehousecode",map.get("storcode"));
						pivo.setAttributeValue("cwarehousename",map.get("storname"));
						pivo.setAttributeValue("cinventorycode",map.get("invcode"));
						pivo.setAttributeValue("invname",map.get("invname"));
//						pivo.setAttributeValue("invspec",map.get(""));
//						pivo.setAttributeValue("invtype",map.get(""));
//						pivo.setAttributeValue("cinvbasid",map.get(""));
//						pivo.setAttributeValue("measdocname",map.get(""));  
						pivo.setAttributeValue("ccalbodycode", map.get("bodycode"));
						pivo.setAttributeValue("ccalbodyname", map.get("bodyname"));
						pivo.setAttributeValue("cinventoryid",map.get("cinventoryid").toString());
						pivo.setAttributeValue("custcode", custcode);
						pivo.setAttributeValue("cvendorid", cvendorid);
						pivo.setAttributeValue("custname", custname);
						
						
						UFDouble num =map.get("num")==null?new UFDouble(0.0):new UFDouble(map.get("num").toString());
			//			Integer num =  map.get("num")==null?new Integer(0):(Integer)map.get("num");
//						Integer freezenum =  map.get("freezenum")==null?new Integer(0):(Integer)map.get("freezenum");
//						num = num+freezenum;
						pivo.setAttributeValue("xcl",num); 
						invxcl.put(map.get("cinventoryid").toString()+map.get("cwarehouseid").toString(), pivo);
					}
					
				}
			}
		}
		
		//将汇总结果封装至list，转换为数组
		HashMap invxclmap = new HashMap();
		invxclmap = clone(invxcl);//对象类型深层copy使用clone，否则会随着inv变化而产生变化

		HashMap map = new HashMap();
 		Iterator it = inv.entrySet().iterator();  
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
 			KlVO svo = (KlVO) entry.getValue();
 			String cinv = svo.getCinventorycode();
			UFDouble price = getInvPrice(cinv,pk_corp);//得到存货最新的单价
			svo.setAttributeValue("nprice", price);
			Map mapday = getVfreeDays(cinv, vfree,pk_corp);//得到钢卷号  
			if(mapday.containsKey(entry.getKey())){
				String day = mapday.get(entry.getKey())==null?"":(String)mapday.get(entry.getKey());
				if(!(day.length()<9)){
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				int days = 0;
				try {
					Date date1 = format.parse(day);
					Date date2 = format.parse(m_sLoginDate);
					days=new Long((date2.getTime()-date1.getTime())/(1000*60*60*24)).intValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int m = 0;m<m_sKlsdday.length;m++){
					String vkey = svo.getCinventoryid()+svo.getCwarehouseclassid();
					if(m==0){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=0&&days<=num){
							
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
						
						
					}else if(m==1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=30&&days<=num){
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
							
						
					}else if(m==2){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=60&&days<=num){
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}					
						
						
						
						
					}else if(m==3){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=90&&days<=num){
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}		
						
						
					}else if(m==4){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=180&&days<=num){
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}	
						
					}else if(m==m_sKlsdday.length-1){
						int num = Integer.parseInt(m_sKlsdday[m]);
						if(days>=num){
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
					}else{
						int num = Integer.parseInt(m_sKlsdday[m]);
						int numbig = Integer.parseInt(m_sKlsdday[m+1]);
						if(days>num&&days<=numbig){
							m=m+1;
							if(map.containsKey(vkey)){
								KlVO xclvo = (KlVO)map.get(vkey);
								Object obj  = xclvo.getAttributeValue(KlVO.SPREV_dynamic_num+m);
								if(obj!=null){
									UFDouble xclnum = (UFDouble)obj;
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													xclnum.add(svo.getXcl())));
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(xclnum.add(svo.getXcl()))));
									/*//现存量金额= 现存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_planmny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													xclnum.add(svo.getXcl())));
									map.put(vkey, xclvo);*/
								}else{  
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
													svo.getXcl())); 
									//库龄库存金额 = 库龄库存量*单价
									xclvo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
											ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
													price.multiply(svo.getXcl())));
									map.put(vkey, xclvo);
								}
								
							}else{
								svo.setAttributeValue(KlVO.SPREV_dynamic_num+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_num+m),
										svo.getXcl()));
								//库龄库存金额 = 库龄库存量*单价
								svo.setAttributeValue(KlVO.SPREV_dynamic_mny+m,
										ICGenVO.addWhenNullAsZero((UFDouble)svo.getAttributeValue(KlVO.SPREV_dynamic_mny+m),
												price.multiply(svo.getXcl())));
								map.put(vkey, svo);
							}
							break;
						}
						
					}
				} 
			  }
			}
		}
		
		Iterator itmap = map.entrySet().iterator();
		List listinv = new ArrayList();
		while (itmap.hasNext()) {
			Map.Entry entry = (Map.Entry) itmap.next();
			KlVO value = (KlVO) entry.getValue();
			if(invxclmap.containsKey(entry.getKey())){
				KlVO vo = (KlVO)invxclmap.get(entry.getKey());
				value.setXcl(vo.getXcl());
			}
			listinv.add(value);
		}
		KlVO[] storvo =(KlVO[]) listinv.toArray(new KlVO[listinv.size()]); 
		return storvo;
	}
	
	
	/**
	 * 获取现存量
	 * @param invcode
	 * @param corp
	 * @param sydate
	 * @param ymdate
	 * @param nowdate
	 * @return
	 */
	public ArrayList getOnhandnum(String corp,String invcode){
		StringBuffer sql = new StringBuffer();
		sql.append("  select a.pk_corp,   ") 
		.append("             a.ccalbodyid,   ") 
		.append("             a.bodycode,   ") 
		.append("             a.bodyname,   ") 
		.append("             a.cwarehouseid,   ") 
		.append("             a.storcode,   ") 
		.append("             a.storname,   ") 
		.append("             a.cinventoryid,   ") 
		.append("             a.invcode,   ") 
		.append("             a.invname,   ") 
		.append("             a.cvendorid, ")
		.append("             a.vbatchcode,   ") 
		.append("             a.vfree1,   ") 
		.append("             a.vfree2,   ") 
		.append("             a.vfree3,   ") 
		.append("             a.vfree4,   ") 
		.append("             a.vfree5,   ") 
		.append("             a.vfree6,   ") 
		.append("             a.vfree7,   ") 
		.append("             a.vfree8,   ") 
		.append("             a.vfree9,   ") 
		.append("             a.vfree10,   ") 
		.append("             a.num,   ") 
		.append("             b.freezenum,   ") 
		.append("             a.ngrossnum,   ") 
		.append("             b.ngrossnum nfreezegrossnum   ") 
		.append("        from (select kp.pk_corp,   ") 
		.append("                     kp.ccalbodyid,   ") 
		.append("                     cal.bodycode,   ") 
		.append("                     cal.bodyname,   ") 
		.append("                     kp.cwarehouseid,   ") 
		.append("                     stor.storcode,   ") 
		.append("                     stor.storname,   ") 
		.append("                     kp.cinventoryid,   ") 
		.append("                     inv.invcode,   ") 
		.append("                     inv.invname,   ") 
		.append("                     kp.cvendorid,  ")
		.append("                     kp.vbatchcode,   ") 
		.append("                     kp.vfree1,   ") 
		.append("                     kp.vfree2,   ") 
		.append("                     kp.vfree3,   ") 
		.append("                     kp.vfree4,   ") 
		.append("                     kp.vfree5,   ") 
		.append("                     kp.vfree6,   ") 
		.append("                     kp.vfree7,   ") 
		.append("                     kp.vfree8,   ") 
		.append("                     kp.vfree9,   ") 
		.append("                     kp.vfree10,   ") 
		.append("                     SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num,   ") 
		.append("                     SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum   ") 
		.append("                from v_ic_onhandnum6 kp, bd_invmandoc man, bd_invbasdoc inv ,bd_calbody cal ,bd_stordoc stor    ") 
		.append("               where kp.cinventoryid = man.pk_invmandoc    ") 
		.append("                 and man.pk_invbasdoc = inv.pk_invbasdoc   ") 
		.append("                 and kp.ccalbodyid = cal.pk_calbody   ") 
		.append("                 and kp.cwarehouseid = stor.pk_stordoc   ") 
		.append("                 and nvl(man.dr,0)=0 and nvl(inv.dr,0)=0    ")
		.append("                 and ((0 = 0) and (inv.invcode='"+invcode+"') and (0 = 0) and (0 = 0) and   ") 
		.append("                     (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and   ") 
		.append("                     (kp.pk_corp = '"+corp+"'))   ") 
		.append("               group by kp.pk_corp,   ") 
		.append("                        kp.ccalbodyid,   ") 
		.append("                        cal.bodycode,   ") 
		.append("                        cal.bodyname,   ") 
		.append("                        kp.cwarehouseid,   ") 
		.append("                        stor.storcode,   ") 
		.append("                        stor.storname,   ") 
		.append("                        kp.cinventoryid,   ") 
		.append("                        inv.invcode,   ") 
		.append("                        inv.invname,   ") 
		.append("                        kp.cvendorid,  ")
		.append("                        kp.vbatchcode,   ") 
		.append("                        kp.vfree1,   ") 
		.append("                        kp.vfree2,   ") 
		.append("                        kp.vfree3,   ") 
		.append("                        kp.vfree4,   ") 
		.append("                        kp.vfree5,   ") 
		.append("                        kp.vfree6,   ") 
		.append("                        kp.vfree7,   ") 
		.append("                        kp.vfree8,   ") 
		.append("                        kp.vfree9,   ") 
		.append("                        kp.vfree10) a,   ") 
		.append("             (select kp.pk_corp,   ") 
		.append("                     kp.ccalbodyid,   ") 
		.append("                     cal.bodycode,   ") 
		.append("                     cal.bodyname,   ") 
		.append("                     kp.cwarehouseid,   ") 
		.append("                     stor.storcode,   ") 
		.append("                     stor.storname,   ") 
		.append("                     kp.cinventoryid,   ") 
		.append("                     inv.invcode,   ") 
		.append("                     inv.invname,   ") 
		.append("                     kp.cvendorid,  ")
		.append("                     kp.vbatchcode,   ") 
		.append("                     kp.vfree1,   ") 
		.append("                     kp.vfree2,   ") 
		.append("                     kp.vfree3,   ") 
		.append("                     kp.vfree4,   ") 
		.append("                     kp.vfree5,   ") 
		.append("                     kp.vfree6,   ") 
		.append("                     kp.vfree7,   ") 
		.append("                     kp.vfree8,   ") 
		.append("                     kp.vfree9,   ") 
		.append("                     kp.vfree10,   ") 
		.append("                     sum(nvl(nfreezenum, 0)) freezenum,   ") 
		.append("                     sum(nvl(ngrossnum, 0)) ngrossnum   ") 
		.append("                from ic_freeze kp, bd_invmandoc man, bd_invbasdoc inv,bd_calbody cal ,bd_stordoc stor    ") 
		.append("               where kp.cinventoryid = man.pk_invmandoc   ") 
		.append("                 and man.pk_invbasdoc = inv.pk_invbasdoc   ") 
		.append("                 and kp.ccalbodyid = cal.pk_calbody   ") 
		.append("                 and kp.cwarehouseid = stor.pk_stordoc   ") 
		.append("                 and nvl(man.dr,0)=0 and nvl(inv.dr,0)=0    ")
		.append("                 and (cthawpersonid is null and (0 = 0) and   ") 
		.append("                     (inv.invcode='"+invcode+"') and (0 = 0) and (0 = 0) and (0 = 0) and   ") 
		.append("                     (0 = 0) and (0 = 0) and (0 = 0) and (kp.pk_corp = '"+corp+"'))   ") 
		.append("               group by kp.pk_corp,   ") 
		.append("                        kp.ccalbodyid,   ") 
		.append("                        cal.bodycode,   ") 
		.append("                        cal.bodyname,   ") 
		.append("                        kp.cwarehouseid,   ") 
		.append("                        stor.storcode,   ") 
		.append("                        stor.storname,   ") 
		.append("                        kp.cinventoryid,   ") 
		.append("                        inv.invcode,   ") 
		.append("                        inv.invname,   ") 
		.append("                        kp.cvendorid,  ")
		.append("                        kp.vbatchcode,   ") 
		.append("                        kp.vfree1,   ") 
		.append("                        kp.vfree2,   ") 
		.append("                        kp.vfree3,   ") 
		.append("                        kp.vfree4,   ") 
		.append("                        kp.vfree5,   ") 
		.append("                        kp.vfree6,   ") 
		.append("                        kp.vfree7,   ") 
		.append("                        kp.vfree8,   ") 
		.append("                        kp.vfree9,   ") 
		.append("                        kp.vfree10) b   ") 
		.append("       where a.pk_corp = b.pk_corp(+)   ") 
		.append("         and a.ccalbodyid = b.ccalbodyid(+)   ") 
		.append("         and a.cwarehouseid = b.cwarehouseid(+)   ") 
		.append("         and a.cinventoryid = b.cinventoryid(+)   ")
		.append("         and a.cvendorid = b.cvendorid(+)    ")
		.append("         and nvl(a.vfree1, ' ') = nvl(b.vfree1(+), ' ')   ") 
		.append("         and nvl(a.vfree2, ' ') = nvl(b.vfree2(+), ' ')   ") 
		.append("         and nvl(a.vfree3, ' ') = nvl(b.vfree3(+), ' ')   ") 
		.append("         and nvl(a.vfree4, ' ') = nvl(b.vfree4(+), ' ')   ") 
		.append("         and nvl(a.vfree5, ' ') = nvl(b.vfree5(+), ' ')   ") 
		.append("         and nvl(a.vfree6, ' ') = nvl(b.vfree6(+), ' ')   ") 
		.append("         and nvl(a.vfree7, ' ') = nvl(b.vfree7(+), ' ')   ") 
		.append("         and nvl(a.vfree8, ' ') = nvl(b.vfree8(+), ' ')   ") 
		.append("         and nvl(a.vfree9, ' ') = nvl(b.vfree9(+), ' ')   ") 
		.append("         and nvl(a.vfree10, ' ') = nvl(b.vfree10(+), ' ')   ") 
		.append("         and (a.num <> 0)  ") ;


		ArrayList list = new ArrayList();
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static <T extends Serializable> T clone(T obj) {
		T clonedObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos
					.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			clonedObj = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clonedObj;

	}
	
	//获取存货最新单价
	public UFDouble getInvPrice(String invcode,String pk_corp){
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.nprice ") 
		.append("   from ia_bill_b b ") 
		.append("  inner join bd_invmandoc man ") 
		.append("     on man.pk_invmandoc = b.cinventoryid ") 
		.append("  inner join bd_invbasdoc bas ") 
		.append("     on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where b.dbizdate in (select max(dbizdate) ") 
		.append("           from (select b.dbizdate, b.nprice, bas.invcode, h.pk_corp ") 
		.append("                   from ia_bill h ") 
		.append("                  inner join ia_bill_b b ") 
		.append("                     on h.cbillid = b.cbillid ") 
		.append("                  inner join bd_invmandoc man ") 
		.append("                     on man.pk_invmandoc = b.cinventoryid ") 
		.append("                  inner join bd_invbasdoc bas ") 
		.append("                     on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("                  where b.pk_corp = '"+pk_corp+"' ") 
		.append("                    and nvl(h.dr, 0) = 0 ") 
		.append("                    and nvl(b.dr, 0) = 0 ") 
		.append("                    and bas.invcode = '"+invcode+"' ") 
		.append("                    and nprice is not null ") 
		.append("                  group by b.dbizdate, b.nprice, bas.invcode, h.pk_corp ") 
		.append("                  order by b.dbizdate, b.nprice, bas.invcode, h.pk_corp)) and b.pk_corp = '"+pk_corp+"'                ") 
		.append("                    and nvl(b.dr, 0) = 0 ") 
		.append("                    and bas.invcode = '"+invcode+"' ") 
		.append("                    and nprice is not null ") ;
		Object price = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			price = uAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		UFDouble nprice = price==null?new UFDouble(0.0):new UFDouble(Double.parseDouble(price.toString()));
		return nprice;
	}
	
	//钢卷号
	public Map getVfreeDays(String invcode,List vfree1,String pk_corp){
		StringBuffer sql = new StringBuffer();
		sql.append(" select min(h.dbilldate) as dbilldate,b.vfree1,b.cinventoryid,h.cwarehouseid  from ic_general_h h ") 
		.append(" left join ic_general_b b ") 
		.append(" on h.cgeneralhid = b.cgeneralhid ") 
		.append(" left join bd_invmandoc man ") 
		.append(" on man.pk_invmandoc = b.cinventoryid  ") 
		.append(" left join bd_invbasdoc bas ") 
		.append(" on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append(" where h.pk_corp = '"+pk_corp+"'  ") 
		.append(" and man.pk_corp = '"+pk_corp+"'")
		.append(" and bas.invcode = '"+invcode+"' ") 
		.append(" and h.dbilldate is not null ")
		.append(" and nvl(h.dr, 0) = 0  ") 
		.append(" and nvl(b.dr, 0) = 0  ") 
		.append(" and nvl(man.dr, 0) = 0  ") 
		.append(" and nvl(bas.dr, 0) = 0  ") 
	//	.append(" and h.cbilltypecode in ('45')  ") 
 	//	.append(" and nvl(b.ninnum,0)>0 ")
 		.append(" and (nvl(b.ninnum,0)>0 or nvl(b.noutnum,0)<0) ")
 		.append(" and (");
        for(int i = 0 ;i<vfree1.size() ;i++){
          if(i == vfree1.size()-1){
            sql.append(" b.vfree1 = '"+vfree1.get(i)+"') ");
          }else{
            sql.append(" b.vfree1 = '"+vfree1.get(i)+"'  or "+"\n");
          }
        };
		sql.append(" group by b.vfree1,b.cinventoryid,h.cwarehouseid");

		ArrayList list = null;
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList) uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map vfree = new HashMap();
		if (list != null && list.size() > 0){ 
			for (int i = 0; i < list.size(); i++) { 
				Map listmap = (Map) list.get(i);  
				String cinventoryid = listmap.get("cinventoryid")==null?"":listmap.get("cinventoryid").toString();
				String cwarehouseid = listmap.get("cwarehouseid")==null?"":listmap.get("cwarehouseid").toString();
				String vfr = listmap.get("vfree1")==null?"":listmap.get("vfree1").toString();
				String key = cinventoryid+cwarehouseid+vfr;
				String value = listmap.get("dbilldate")==null?"":listmap.get("dbilldate").toString();
				vfree.put(key, value);
			}
		}
		 
		return vfree;
	}
}
