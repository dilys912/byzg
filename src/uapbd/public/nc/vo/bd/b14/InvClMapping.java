
package nc.vo.bd.b14;
/**
 * 存货分类基本档案VO
 * @author yhj 2014-02-22
 */
import nc.jdbc.framework.mapping.IMappingMeta;

public class InvClMapping
    implements IMappingMeta
{

    public InvClMapping()
    {
    }

    public String getPrimaryKey()
    {
        return "pk_invcl";
    }

    public String getTableName()
    {
        return "bd_invcl";
    }

    public String[] getAttributes()
    {
        return ATTRIBUTES;
    }

    public String[] getColumns()
    {
        return ATTRIBUTES;
    }

    private static final String BD_INVCL = "bd_invcl";
    private static final String PK_INVCL = "pk_invcl";
    private static final String ATTRIBUTES[] = {
    	"memo","pk_invcl", "invclasscode", "invclassname", "invclasslev", "avgprice", "endflag", "averagecost", "averagepurahead", "averagemmahead", "pk_corp", 
        "sealdate"
    };
    private static final long serialVersionUID = -8916242912418026207L;

}

