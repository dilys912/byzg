package nc.bs.by.invapp.h0h002;

import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.naming.NamingException;

import nc.bs.bd.service.BDOperateServ;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.SystemException;
import nc.bs.trade.business.HYPubBO;
import nc.impl.uap.bd.produce.ProduceDAO;
import nc.vo.bd.b431.ProduceVO;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.by.invapp.h0h002.ConvertVO;
import nc.vo.by.invapp.h0h002.DCONVERTVO;
import nc.vo.by.invapp.h0h002.INVBASDOCVO;
import nc.vo.by.invapp.h0h002.INVMANDOCVO;
import nc.vo.by.invapp.h0h002.InvappdocVO;
import nc.vo.by.invapp.h0h002.PRODUCEVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;

public class ApproveBill {
	
	private ProduceDAO produceDAO;

	public void saveInvManAndProduce(PfParameterVO vo) throws BusinessException, SystemException, SQLException, NamingException {
		if(vo.m_billType.equals("TA02")){
			Object inObject = vo.m_preValueVo;
			if (inObject == null) {
				throw new nc.vo.pub.BusinessException("错误：物料申请单没有数据！");
			}
			saveManAndProduce(inObject);
		}else{
			throw new nc.vo.pub.BusinessException("错误：单据类型取值不对！");
		}
	}

	private void saveManAndProduce(Object inObject) throws BusinessException, SystemException, SQLException, NamingException {
		if(inObject instanceof nc.vo.by.invapp.h0h002.MyExAggVO){
			nc.vo.by.invapp.h0h002.MyExAggVO sov = new nc.vo.by.invapp.h0h002.MyExAggVO();
			sov = (nc.vo.by.invapp.h0h002.MyExAggVO) inObject;
			InvappdocVO	hvo = (nc.vo.by.invapp.h0h002.InvappdocVO) sov.getParentVO();
			ConvertVO[]	bvo = (nc.vo.by.invapp.h0h002.ConvertVO[]) sov.getTableVO("bd_convert_app");
			//存货基本档案
			if (hvo!=null) {
				HYPubBO bo = new HYPubBO();
				InvbasdocVO invbas = new InvbasdocVO();
				Field[] finvbas = InvbasdocVO.class.getFields();
				for (int i = 0; i < finvbas.length; i++) {
					String fname = finvbas[i].getName();
					if (!fname.equals("ts")) {
						invbas.setAttributeValue(fname, hvo.getAttributeValue(fname));
					}
				}
				invbas.setPk_corp("0001");
				String pk_invbasdoc = bo.insert(invbas);
				invbas.setPrimaryKey(pk_invbasdoc);
				if (bvo!=null&&bvo.length>0) {
					Field[] cinvbas = nc.vo.bd.invdoc.ConvertVO.class.getFields();
					nc.vo.bd.invdoc.ConvertVO[] cvos = new nc.vo.bd.invdoc.ConvertVO[bvo.length];
					for (int i = 0; i < bvo.length; i++) {
						nc.vo.bd.invdoc.ConvertVO cvoi = new nc.vo.bd.invdoc.ConvertVO();
						ConvertVO bvoi = bvo[i];
						for (int j = 0; j < cinvbas.length; j++) {
							String cname = cinvbas[j].getName();
							if (!cname.equals("pk_convert")&&!cname.equals("ts")) {
								cvoi.setAttributeValue(cname, bvoi.getAttributeValue(cname));
							}
						}
						cvoi.setPk_invbasdoc(pk_invbasdoc);
						cvos[i] = cvoi;
					}
					bo.insertAry(cvos);
				}
				//存货管理档案
				InvmandocVO invmanvo = assignInvbasdocToInvmandoc(invbas,hvo);
				String pk_invmandoc = bo.insert(invmanvo);
				invmanvo.setPrimaryKey(pk_invmandoc);
				//物料生产档案
				Object pk_calbody =  hvo.getPk_calbody();
				String pk_produce = "";
				if (pk_calbody!=null) {
					ProduceVO produce = assignInvmandocToProduce(invmanvo,hvo);
					pk_produce = produce.getPk_produce();
				}
				//更新存货基本档案主键、存货管理档案主键、物料生产档案主键到物料申请单，以便弃审时删除三个关联档案。
				hvo.setPk_invbasdoc(pk_invbasdoc);
				hvo.setPk_invmandoc(pk_invmandoc);
				hvo.setPk_produce(pk_produce);
				bo.update(hvo);
			}
		}
	}

	/**
	 * 物料生产档案
	 * @throws BusinessException 
	 * @throws NamingException 
	 * @throws SQLException 
	 * @throws SystemException 
	 * */
	private ProduceVO assignInvmandocToProduce(InvmandocVO invmanvo, InvappdocVO hvo) throws BusinessException, SystemException, SQLException, NamingException {
		ProduceVO[] produceVOs = new ProduceVO[1];
		ProduceVO producevo = new ProduceVO(); 
		String[] pname = producevo.getAttributeNames();
		for (int i = 0; i < pname.length; i++) {
			producevo.setAttributeValue(pname[i], invmanvo.getAttributeValue(pname[i]));
		}
		for (int i = 0; i < pname.length; i++) {
			if (!pname[i].equals("pk_corp")&&!pname[i].equals("pk_invbasdoc")&&!pname[i].equals("pk_invmandoc")&&!pname[i].equals("ts")) {
				if (pname[i].indexOf("def")!=-1) {
					invmanvo.setAttributeValue(pname[i], hvo.getAttributeValue("sc"+pname[i]));
				}else{
					producevo.setAttributeValue(pname[i], hvo.getAttributeValue(pname[i]));
				}
			}
		}
		producevo.setPk_calbody(hvo.getPk_calbody());
		produceVOs[0] = producevo;
		
		// 构造校验服务
		BDOperateServ opServer = new BDOperateServ();
		// 调用事前校验
		for (int j = 0; produceVOs != null && j < produceVOs.length; j++) {
			ProduceVO produceVO = produceVOs[j];
			opServer.beforeOperate("10081208", 16, produceVO.getPk_invmandoc(),
					produceVO.getPk_calbody(), produceVO);
		}

		String[] keys = null;

		keys = getProduceDAO().insertArray(produceVOs);

		// 调用事后校验
		for (int j = 0; produceVOs != null && j < produceVOs.length; j++) {
			ProduceVO produceVO = produceVOs[j];
			produceVO.setPrimaryKey(keys[j]);
			opServer.afterOperate("10081208", 16, produceVO.getPk_invmandoc(),
					produceVO.getPk_calbody(), produceVO);
		}
		produceVOs[0].setPrimaryKey(keys[0]);
		return produceVOs[0];
	}

	/**
	 * 存货管理档案
	 * */
	private InvmandocVO assignInvbasdocToInvmandoc(InvbasdocVO invbas, InvappdocVO hvo) {
		InvmandocVO invmanvo = new InvmandocVO();
		String[] invman = invmanvo.getAttributeNames();
		for (int i = 0; i < invman.length; i++) {
			String mname = invman[i];
			invmanvo.setAttributeValue(mname, invbas.getAttributeValue(mname));
		}
		for (int i = 0; i < invman.length; i++) {
			String mname = invman[i]; 
			if (!mname.equals("pk_invbasdoc")&&!mname.equals("ts")) {
				if (mname.equals("purchasestge")) {
					invmanvo.setPurchasestge(hvo.getPurchasestge()==null?"":hvo.getPurchasestge().toString());
				}else if (mname.indexOf("def")!=-1) {
					invmanvo.setAttributeValue(mname, hvo.getAttributeValue("gl"+mname));
				}else if (mname.indexOf("free")!=-1&&!mname.equals("isinvretfreeofchk")) {
					invmanvo.setAttributeValue(mname, hvo.getAttributeValue("gl"+mname));
				}else{
					invmanvo.setAttributeValue(mname, hvo.getAttributeValue(mname));
				}
			}
		}
		invmanvo.setPk_corp(hvo.getPk_corp_app());
		invmanvo.setSealflag(new UFBoolean(false));
		return invmanvo;
	}
	
	public ProduceDAO getProduceDAO() throws SystemException, NamingException {
		if (produceDAO == null) {
			produceDAO = new ProduceDAO();
		}
		return produceDAO;
	}

	public void deleteInvManAndProduce(PfParameterVO vo) throws BusinessException {
		if(vo.m_billType.equals("TA02")){
			Object inObject = vo.m_preValueVo;
			if (inObject == null) {
				throw new nc.vo.pub.BusinessException("错误：物料申请单没有数据！");
			}
			deleteManAndProduce(inObject);
		}else{
			throw new nc.vo.pub.BusinessException("错误：单据类型取值不对！");
		}
	}

	private void deleteManAndProduce(Object inObject) throws DAOException {
		if(inObject instanceof nc.vo.by.invapp.h0h002.MyExAggVO){
			BaseDAO baseDAO = new BaseDAO();
			nc.vo.by.invapp.h0h002.MyExAggVO sov = new nc.vo.by.invapp.h0h002.MyExAggVO();
			sov = (nc.vo.by.invapp.h0h002.MyExAggVO) inObject;
			InvappdocVO	hvo = (nc.vo.by.invapp.h0h002.InvappdocVO) sov.getParentVO();
			Object pk_invappdoc = hvo.getPk_invappdoc();
			Object pk_invbasdoc = hvo.getPk_invbasdoc();
			Object pk_invmandoc = hvo.getPk_invmandoc();
			Object pk_produce = hvo.getPk_produce();
			if (pk_produce!=null) {
				String sql3 = " pk_produce = '"+pk_produce+"' ";
				baseDAO.deleteByClause(PRODUCEVO.class, sql3);
			}
			if (pk_invmandoc!=null) {
				String sql2 = " pk_invmandoc = '"+pk_invmandoc+"' ";
				baseDAO.deleteByClause(INVMANDOCVO.class,sql2);
			}
			if (pk_invbasdoc!=null) {
				String sql12 = " pk_invbasdoc = '"+pk_invbasdoc+"' ";
				baseDAO.deleteByClause(DCONVERTVO.class,sql12);
			}
			if (pk_invbasdoc!=null) {
				String sql1 = " pk_invbasdoc = '"+pk_invbasdoc+"' ";
				baseDAO.deleteByClause(INVBASDOCVO.class,sql1);
			}
			if (pk_invappdoc!=null) {
				String sql4 = "update bd_invappdoc set pk_invbasdoc = null,pk_invmandoc = null," +
						"pk_produce = null where nvl(dr,0) = 0 and pk_invappdoc = '"+pk_invappdoc+"'";
				baseDAO.executeUpdate(sql4);
			}
		}
	}

}
