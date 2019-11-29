package nc.ui.mo.mo6601;

import java.util.Vector;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.fp.fp401.CrossVO;
import nc.vo.pub.ValueObject;

public class RefGlModel extends AbstractRefModel
{
	private String title;

	public RefGlModel(String title)
	{
		super();
		this.title = title == null ? "" : title;
		initial();
	}

	@Override
	public String getRefTitle()
	{
		return title;
	}

	private void initial()
	{
		if (this.getRefTitle().equalsIgnoreCase("下线隔离单参照Downline isolated single reference"))
		{
			initialXiaXian();
		} else if (this.getRefTitle().equalsIgnoreCase("库内隔离单参照Storehouse isolated single reference"))
		{
			initialKuNeiGeLi();
		}
	}

	private void initialXiaXian()
	{
		super.setTableName("mm_glzb a,mm_glzb_b b");
		super.setPkFieldCode("pk_glzb_b");
		StringBuffer wherePart = new StringBuffer(" a.pk_glzb=b.pk_glzb");
		wherePart.append("  and a.djzt=0 and a.jyjg=1 and a.billsign='JYDJ'");
		wherePart.append(" and a.pk_corp='").append(
				ClientEnvironment.getInstance().getCorporation().getPk_corp())
				.append("'");
		wherePart.append(" and nvl(b.clzt,0)!=1");
		wherePart.append(" and nvl(a.dr,0)!='1'");
		wherePart.append(" and nvl(b.dr,0)!='1'");
//		wherePart.append(" and b.dh not in (select cldh from mm_glcl_b where nvl(dr,0)=0)");
		super.setWherePart(wherePart.toString());
		super.setRefCodeField("b.pk_glzb_b");
		super.setRefNameField("b.cp");
		super.setHiddenFieldCode(new String[] { "b.pk_glzb", "b.pk_glzb_b",
				"a.zdsl","a.ck","cspaceid","cinventoryid","scx","bz" });
		super.setFieldCode(new String[] { "b.cp", "b.dh", "b.glyy", "b.hw",
				// "b.hwmc",
				"b.lh", "b.ph", "b.xxaglsl", "a.zdsl"});//edit by zwx 整垛数量显示 2019年9月23日
		super.setFieldName(new String[] { "产品", "垛号", "隔离原因", "货位",
				// "货位名称",
				"料号", "批号", "下线\\隔离数量","整垛数量" });//edit by zwx 整垛数量显示 2019年9月23日
		super.setDefaultFieldCount(super.getFieldCode().length);
	}
	
	private void initialKuNeiGeLi()
	{
		super.setTableName("mm_glzb a,mm_glzb_b b");
		super.setPkFieldCode("pk_glzb_b");
		StringBuffer wherePart = new StringBuffer(" a.pk_glzb=b.pk_glzb");
		wherePart.append("  and nvl(a.djzt,0)=0  and a.billsign='isolation'");
		wherePart.append(" and a.pk_corp='").append(
				ClientEnvironment.getInstance().getCorporation().getPk_corp())
				.append("'");
		wherePart.append(" and nvl(b.clzt,0)!=1");
		wherePart.append(" and nvl(a.dr,0)!='1'");
		wherePart.append(" and nvl(b.dr,0)!='1'");
		super.setWherePart(wherePart.toString());
		super.setRefCodeField("b.pk_glzb_b");
		super.setRefNameField("b.cp");
		super.setHiddenFieldCode(new String[] { "b.pk_glzb", "b.pk_glzb_b",
				"a.zdsl","b.isolationckid" });
		super.setFieldCode(new String[] { "b.cp", "b.dh", "b.glyy", "b.hw",
				// "b.hwmc",
				"b.lh", "b.ph", "b.xxaglsl", });
		super.setFieldName(new String[] { "产品", "垛号", "隔离原因", "货位",
				// "货位名称",
				"料号", "批号", "下线\\隔离数量", });
		super.setDefaultFieldCount(super.getFieldCode().length);
	}

	public String getWherePart()
	{
		String sql = super.getWherePart();
		return sql;
	}

	public String getRefSql()
	{
		String sql = super.getRefSql();
		System.out.println("参照SQL：" + sql);
		return sql;
	}

	String[] allField = null;
	private String[] getAllField()
	{
		if(allField==null||allField.length==0)
		{
			String[] fields = this.getFieldCode();
			String[] hidFields = this.getHiddenFieldCode();
			int length  = fields.length;
			if(hidFields!=null&&hidFields.length>0)
			{
				length += hidFields.length;
			}
			 
			allField = new String[length];
			for(int i=0;i<length;i++)
			{
				if(i<fields.length)
				{
					allField[i] = fields[i];
				}else
				{
					allField[i] = hidFields[i-fields.length];
				}
			}
			return allField;
		}else
		{
			return allField;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ValueObject convertToVO(Vector vData)
	{
		String[] allField = this.getAllField();
		CrossVO obj = new CrossVO();
		String[] temp = null;
		for(int i=0;i<allField.length;i++)
		{
			temp = allField[i].split("\\.");
			if(temp.length==2)
			{
				obj.setAttributeValue(temp[1], vData.get(i));
			}else
			{
				obj.setAttributeValue(allField[i], vData.get(i));
			}
		}
		return obj;
	}

}
