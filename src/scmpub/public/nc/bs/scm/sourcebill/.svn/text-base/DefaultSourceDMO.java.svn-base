package nc.bs.scm.sourcebill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.itf.scm.sourcebill.ISourceBillFinder;
import nc.vo.scm.sourcebill.LightBillVO;
import nc.vo.scm.sourcebill.SourceBillParaV3;

public class DefaultSourceDMO extends DataManageObject implements ISourceBillFinder {
	public DefaultSourceDMO() throws NamingException, SystemException {
	}

	public DefaultSourceDMO(String dbName) throws NamingException, SystemException {
		super(dbName);
	}

	private String createSQL(String billType) {
		String[] info = SourceBillParaV3.getSourceInfo(billType);
		if (info == null) { return null; }
		String hTable = info[0];
		String hPkField = info[1];
		String hTableCodeField = info[2];
		String bTable = info[4];
		String bFkField = info[5];
		String bTableSourceTypeField = info[6];
		String bTableSourceIDField = info[7];
		String sBillPkcorp = info[16];

		if ((bTableSourceTypeField == null) || (bTableSourceTypeField.trim().length() == 0) || (bTableSourceIDField == null) || (bTableSourceIDField.trim().length() == 0)) {
			System.out.println("Hint:this bill has no src." + billType);
			return null;
		}
		StringBuffer sb = new StringBuffer("SELECT DISTINCT");
		sb.append(" ");
		sb.append(hTable + "." + hTableCodeField);
		sb.append(", ");

		if (hTable.equalsIgnoreCase(bTable)) sb.append("B." + bTableSourceTypeField);
		else {
			sb.append(bTable + "." + bTableSourceTypeField);
		}
		sb.append(", ");
		if (hTable.equalsIgnoreCase(bTable)) sb.append("B." + bTableSourceIDField);
		else {
			sb.append(bTable + "." + bTableSourceIDField);
		}

		sb.append(", ");
		sb.append(hTable + "." + sBillPkcorp);
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(hTable);
		sb.append(", ");

		if (hTable.equalsIgnoreCase(bTable)) sb.append(bTable + " B");
		else {
			sb.append(bTable);
		}
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append("=");
		if (hTable.equalsIgnoreCase(bTable)) sb.append("B." + bFkField);
		else {
			sb.append(bTable + "." + bFkField);
		}

		sb.append(" and ");
		sb.append(hTable + "." + hPkField);
		sb.append("=?");
		sb.append(" and ");
		sb.append(hTable + ".dr =0");

		sb.append(" and ");
		if (hTable.equalsIgnoreCase(bTable)) sb.append("B.dr =0");
		else {
			sb.append(bTable + ".dr =0");
		}

		return sb.toString();
	}

	private String createSQL1(String billType) {
		String[] info = SourceBillParaV3.getSourceInfo(billType);
		if (info == null) { return null; }
		String hTable = info[0];
		String hPkField = info[1];
		String hTableCodeField = info[2];
		String hTableBillTypeField = info[3];
		String bTable = info[4];
		String bFkField = info[5];
		String bTableSourceTypeField = info[6];
		String bTableSourceIDField = info[7];
		String sBillPkcorp = info[16];

		if ((bTableSourceTypeField == null) || (bTableSourceIDField == null)) { return null; }

		StringBuffer sb = new StringBuffer("SELECT DISTINCT");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append(", ");
		sb.append(hTable + "." + hTableCodeField);
		// edit by zip: 2014/3/20
		// common error
		if(sBillPkcorp != null && !"".equals(sBillPkcorp)) {
			sb.append(", ");
			sb.append(hTable + "." + sBillPkcorp);
		}else {
			sb.append(",'' as pk_corp");
		}
		// edit end
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(hTable);
		sb.append(", ");
		if (hTable.equalsIgnoreCase(bTable)) sb.append(bTable + " B");
		else {
			sb.append(bTable);
		}
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append("=");

		if (hTable.equalsIgnoreCase(bTable)) sb.append("B." + bFkField);
		else {
			sb.append(bTable + "." + bFkField);
		}

		sb.append(" and ");
		if (hTable.equalsIgnoreCase(bTable)) sb.append("B." + bTableSourceIDField);
		else {
			sb.append(bTable + "." + bTableSourceIDField);
		}
		sb.append("=?");

		sb.append(" and ");
		if (hTable.equalsIgnoreCase(bTable)) sb.append("rtrim(B." + bTableSourceTypeField + ")");
		else {
			sb.append("rtrim(" + bTable + "." + bTableSourceTypeField + ")");
		}
		sb.append("=?");

		if (hTableBillTypeField != null) {
			sb.append(" and ");
			sb.append(hTable + "." + hTableBillTypeField);
			sb.append("=?");
		}

		sb.append(" and ");
		sb.append(hTable + ".dr =0");
		sb.append(" and ");

		if (hTable.equalsIgnoreCase(bTable)) sb.append("B.dr =0");
		else {
			sb.append(bTable + ".dr =0");
		}

		return sb.toString();
	}

	private String createSQL2(String billType) {
		String[] info = SourceBillParaV3.getSourceInfo(billType);
		if (info == null) { return null; }
		String hTable = info[0];
		String hPkField = info[1];
		String hTableCodeField = info[2];

		if ((hTableCodeField == null) || (hTableCodeField.trim().length() == 0)) {
			System.out.println("HINT:no billcodefield." + billType);
			return null;
		}

		StringBuffer sb = new StringBuffer("SELECT");
		sb.append(" ");
		sb.append(hTable + "." + hTableCodeField);
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(hTable);
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append("=?");

		return sb.toString();
	}

	protected String getBillCode(String curBillType, String curBillID) {
		beforeCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillCode", new Object[] {
				curBillType, curBillID
		});

		String sql = createSQL2(curBillType);
		if (sql == null) { return "##"; }
		String code = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, curBillID);

			rs = stmt.executeQuery();

			if (rs.next()) code = rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		afterCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillCode", new Object[] {
				curBillType, curBillID
		});

		return code;
	}

	public String getBillTempletID(String billType) throws SQLException {
		return null;
	}

	public String getBillTempletID(String billType, String pk_corp, String userID) throws SQLException {
		beforeCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillTempletID", new Object[] {
			billType
		});

		String sql = "select templateid from pub_systemplate A,bd_billtype B where A.funnode = B.nodecode and A.tempstyle = 0 and B.pk_billtypecode = ? ";

		String templateID = null;

		if (billType == null) { return null; }
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, billType);

			rs = stmt.executeQuery();

			if (rs.next()) templateID = rs.getString(1);
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		afterCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillTempletID", new Object[] {
			billType
		});

		return templateID;
	}

	public String getBillTypeName(String billType) throws SQLException {
		beforeCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillTypeName", new Object[] {
			billType
		});

		if (billType == null) { return null; }
		String sql = "select billtypename from bd_billtype where pk_billtypecode = ? ";
		String typeName = null;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, billType);

			rs = stmt.executeQuery();

			if (rs.next()) typeName = rs.getString(1);
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		afterCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillTypeName", new Object[] {
			billType
		});

		return typeName;
	}

	protected String getBillUI(String curBillType) {
		beforeCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillUI", new Object[] {
			curBillType
		});

		String sql = "SELECT A.class_name FROM sm_funcregister A INNER JOIN bd_billtype B ON A.fun_code = B.nodecode WHERE B.pk_billtypecode = ? ";

		String ui = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, curBillType);

			rs = stmt.executeQuery();

			if (rs.next()) ui = rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		afterCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getBillUI", new Object[] {
			curBillType
		});

		return ui;
	}

	public LightBillVO[] getForwardBills(String curBillType, String curBillID, String forwardBillType) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector vResult = new Vector();
		try {
			String sql = createSQL1(forwardBillType);
			if (sql == null) {
				System.out.println("ERROR:bill info is not registered,sql is null:" + forwardBillType);
				return null;
			}
			String[] info = SourceBillParaV3.getSourceInfo(forwardBillType);
			if (info == null) {
				System.out.println("ERROR:bill info is not registered:" + forwardBillType);
				return null;
			}
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, curBillID);
			stmt.setString(2, curBillType);
			String billTypeField = info[3];
			if (billTypeField != null) {
				if (forwardBillType.startsWith("D")) {
					stmt.setString(3, info[18]);
				} else stmt.setString(3, forwardBillType);

			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				String code = rs.getString(2);

				String billPkcorp = rs.getString(3);
				if (id != null) {
					LightBillVO svo = new LightBillVO();
					svo.setID(id);
					svo.setCode(code);
					svo.setType(forwardBillType);
					svo.setBillcorp(billPkcorp);
					vResult.add(svo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
			}
		}
		LightBillVO[] svos = null;
		if (vResult.size() > 0) {
			svos = new LightBillVO[vResult.size()];
			vResult.copyInto(svos);
		}

		return svos;
	}

	public LightBillVO[] getSourceBills(String curBillType, String curBillID) {
		beforeCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getSourceBills", new Object[] {
				curBillType, curBillID
		});

		String sql = createSQL(curBillType);
		if (sql == null) return null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, curBillID);

			rs = stmt.executeQuery();

			while (rs.next()) {
				String type = rs.getString(2);
				String id = rs.getString(3);

				String billcorp = rs.getString(4);
				if ((type != null) && (id != null) && (type.trim().length() > 0) && (id.trim().length() > 0)) {
					LightBillVO svo = new LightBillVO();
					svo.setType(type);
					svo.setID(id);
					svo.setBillcorp(billcorp);
					v.add(svo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null) con.close();
			} catch (Exception e) {
			}
		}
		LightBillVO[] vos = null;
		if (v.size() > 0) {
			vos = new LightBillVO[v.size()];
			v.copyInto(vos);
			for (int i = 0; i < vos.length; i++) {
				vos[i].setCode(getBillCode(vos[i].getType(), vos[i].getID()));
			}

		}

		afterCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getSourceBills", new Object[] {
				curBillType, curBillID
		});

		return vos;
	}
}