package nc.ui.bd.ref;

public abstract interface IBusiType
{
  public static final int GRID = 0;
  public static final int TREE = 1;
  public static final int GRIDTREE = 2;
  public static final int GRID_SPECIAL = 3;
  public static final int GRID_MULTILANG = 4;
  public static final String[] BUSITYPE = { "人员基本档案", "人员档案", "部门档案", 
    "公司目录(集团)S", "公司目录", "公司目录(权限)", "公司目录(集团)", "客商基本档案", "客商档案", "客户档案", "供应商档案", //add by shikun 2014-10-30 公司目录依据权限查询
    "客商档案包含冻结", "客户档案包含冻结", "供应商档案包含冻结", "客商辅助核算", "客户辅助核算", "供应商辅助核算", 
    "存货基本档案", "存货档案", "会计科目", "人员类别", "存货分类", "凭证类别", "收付款协议", "结算方式", 
    "开户银行", "开户银行1", "开户银行2", "开户银行3", "仓库档案", "发运方式", "收发类别", "地区分类", 
    "常用摘要", "公用自定义项", "公用自定义项(不包含引用基本档案)", "收支项目", "计量档案", "税目税率", 
    "项目类型", "项目档案", "项目管理档案", "项目管理档案表格", "货位档案", "外币档案", "客商发货地址", 
    "自定义项档案", "成套件", "会计期间", "业务类型", "库存组织", "权限操作员", "操作员", "物料档案", 
    "采购组织", "帐龄区间", "单据类型", "帐户", "收入项目", "支出项目", "客商档案(并集)", "权限公司目录", 
    "权限公司目录(集团)", "销售组织", "现金流量项目", "自定义项档案列表", "票据类型", "票据类型1", 
    "票据类型2", "票据类型3", "票据类型4", "票据类型5", "账户档案", "账户档案1", "人员档案HR", 
    "基础档案资源列表", "产品线档案", "公司类别", "结算单位", "地点档案", "结算中心", "用户组", 
    "会计期间方案", "科目方案", "核算账簿", "会计主体", "主体账簿", "日历", "计算器", "颜色", 
    "部门档案HR", "集团会计科目", "会计科目多版本", "账簿主体", "结算单位树表", "内部账户", 
    "固定资产核算账簿", "固定资产账簿公司", "角色", "仓库档案多公司", "库存组织多公司", "生产订单","BOM版本" };

  public static final String[] BUSITYPE_RESID = { "UPPref-000514", 
    "UPPref-000443", "UPPref-000005", "UPPref-000002", "UPPref-000004", "公司目录(权限)", //add by shikun 2014-10-30 公司目录依据权限查询
    "UPPref-000000", "UC000-0001581", "UC000-0001584", "UPPref-000444", 
    "UPPref-000445", "UPPref-000446", "UPPref-000447", "UPPref-000448", 
    "UC000-0001584", "UPPref-000444", "UPPref-000445", "UPPref-000367", 
    "UPPref-000449", "UPPref-000006", "UC000-0000140", "UC000-0001443", 
    "UC000-0000479", "UC000-0002200", "UC000-0003249", "UC000-0001896", 
    "UPPref-000450", "UPPref-000451", "UPPref-000452", "UC000-0000160", 
    "UC000-0001024", "UC000-0002209", "UC000-0001235", "UPPref-000453", 
    "UPPref-000454", "UPPref-000454", "UC000-0002217", "UPPref-000455", 
    "UPPref-000456", "UPPref-000380", "UPPref-000457", "UC000-0004175", 
    "UC000-0004175", "UPPref-000459", "UPPref-000460", "UPPref-000461", 
    "UPPref-000462", "UC000-0001953", "UC000-0000240", "UC001-0000003", 
    "UC000-0001825", "UPPref-000463", "UC000-0002188", "UPPref-000464", 
    "UC000-0004091", "UPPref-000465", "UC000-0000807", "UC000-0001766", 
    "UPPref-000466", "UPPref-000467", "UPPref-000468", "UPPref-000469", 
    "UPPref-000469", "UC000-0004128", "UC000-0002922", "UPPref-000470", 
    "UC000-0003020", "UPPref-000472", "UPPref-000473", "UPPref-000474", 
    "UPPref-000475", "UPPref-000476", "UPPref-000477", "UPPref-000477", 
    "UPPref-000478", "UPPref-000479", "UPPref-000480", "UPPref-000481", 
    "UC000-0003242", "UPPref-000482", "UC000-0003234", "UPPref-000483", 
    "UPPref-000429", "UPPref-000428", "UPPref-000484", "UPPref-000383", 
    "UPPref-000485", "UPPref-000486", "UPPref-000487", "UPPref-000488", 
    "UPPref-000005", "UPPref-000510", "UPPref-000511", "UPPref-000509", 
    "UPPref-000508", "UPPref-000507", "UPPref-000512", "UPPref-000513", 
    "UPPref-000504", "UPPref-000515", "UPPref-000516", "生产订单","BOM版本" };

  public static final String[] TABLENAME = { "bd_psnbasdoc", "bd_psndoc", 
    "bd_deptdoc", "bd_corp", "bd_corp", "bd_corp", "bd_corp", "bd_cubasdoc", 
    "bd_cumandoc", "bd_cumandoc", "bd_cumandoc", "bd_cumandoc", 
    "bd_cumandoc", "bd_cumandoc", "bd_cumandoc", "bd_cumandoc", 
    "bd_cumandoc", "bd_invbasdoc", "bd_invmandoc", "bd_accsubj", 
    "bd_psncl", "bd_invcl", "bd_vouchertype", "bd_payterm", 
    "bd_balatype", "bd_accbank", "bd_accbank", "bd_accbank", 
    "bd_accbank", "bd_stordoc", "bd_sendtype", "bd_rdcl", "bd_areacl", 
    "bd_comabstr", "bd_defdef", "bd_defdef", "bd_costsubj", 
    "bd_measdoc", "bd_taxitems", "bd_jobtype", "bd_jobbasfil", 
    "bd_jobmngfil", "bd_jobmngfil", "bd_cargdoc", "bd_currtype", 
    "bd_custaddr", "bd_defdoc", "bd_setpart", "bd_accperiod", 
    "bd_busitype", "bd_calbody", "sm_user", "sm_user", 
    "bd_produce,bd_invbasdoc", "bd_purorg", "pub_timecontrol", 
    "arap_djlx", "bd_accid", "bd_costsubj", "bd_costsubj", 
    "bd_cumandoc", "bd_corp", "bd_corp", "bd_salestru", "bd_cashflow", 
    "bd_defdoclist", "arap_note_type", "arap_note_type", 
    "arap_note_type", "arap_note_type", "arap_note_type", 
    "arap_note_type", "bd_accid", "bd_accid", "bd_psndoc", "bd_bdinfo", 
    "bd_prodline", "bd_corpkind", "bd_settleunit", "bd_address", 
    "bd_corp", "sm_group", "bd_accperiodscheme", "bd_subjscheme", 
    "bd_glbook", "bd_glorg", "bd_glorgbook", "bd_deptdoc", 
    "bd_accsubj", "bd_accsubj", "bd_glorgbook", "bd_settleunit", 
    "bd_accid", "bd_glbook", "bd_glorgbook", "sm_role", "bd_stordoc", 
    "bd_calbody", "mm_mo","bd_bom" };

  public static final int[] BUSITYPE_INDEX = { 0, 2, 1, 1, 1,1, 1, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 
    0, 0, 0, 1, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 
    0, 0, 3, 0, 0, 0, 2, 1, 1, 0, 1, 0, 3, 3, 3, 3, 3, 3, 0, 0, 2, 4, 
    0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 2,0,0,0,0,0 };

  public static final String[] IMPL_CLASSNAME = { 
    "nc.ui.bd.ref.busi.PsnbasdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.PsndocDefaulRefModel", 
    "nc.ui.bd.ref.busi.DeptdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel", 
    "nc.ui.bd.ref.busi.CorpDefaultRefModel", 
    "nc.ui.bd.ref.busi.CorpDefaultRefModel2", //add by shikun 2014-10-30 公司目录依据权限查询
    "nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustbasdocDefaulteRefModel", 
    "nc.ui.bd.ref.busi.CustmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocAssDefaultRefModel", 
    "nc.ui.bd.ref.busi.InvbasdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.InvmandocDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccsubjDefaultRefModel", 
    "nc.ui.bd.ref.busi.PsnclDefaultRefModel", 
    "nc.ui.bd.ref.busi.InvclDefaultRefModel", 
    "nc.ui.bd.ref.busi.VoucherTypeDefaultRefModel", 
    "nc.ui.bd.ref.busi.PaytermDefaultRefModel", 
    "nc.ui.bd.ref.busi.BalanceTypeDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccBankDefaulteRefModel", 
    "nc.ui.bd.ref.busi.AccBank123RefModel", 
    "nc.ui.bd.ref.busi.AccBank123RefModel", 
    "nc.ui.bd.ref.busi.AccBank123RefModel", 
    "nc.ui.bd.ref.busi.StorDocDefaulteRefModel", 
    "nc.ui.bd.ref.busi.SendTypeDefaultRefModel", 
    "nc.ui.bd.ref.busi.RdclDefaultRefModel", 
    "nc.ui.bd.ref.busi.AreaclDefaultRefModel", 
    "nc.ui.bd.ref.busi.ComAbstrDefaultRefModel", 
    "nc.ui.bd.ref.busi.DefdefDefaultRefModel", 
    "nc.ui.bd.ref.busi.DefdefDefaultRefModel", 
    "nc.ui.bd.ref.busi.CostsubjDefaultRefModel", 
    "nc.ui.bd.ref.busi.MeasdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.TaxDefaultRefModel", 
    "nc.ui.bd.ref.busi.JobtypeDefaultRefModel", 
    "nc.ui.bd.ref.busi.JobbasfilDefaultRefModel", 
    "nc.ui.bd.ref.busi.JobmngfilDefaultRefModel", 
    "nc.ui.bd.ref.busi.JobmngfilgridRefModel", 
    "nc.ui.bd.ref.busi.CargdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.CurrtypeDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustAddrDefaultRefModel", 
    "nc.ui.bd.ref.busi.DefdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.SetPartDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccPeriodDefaultRefModel", 
    "nc.ui.bd.ref.busi.BusiTypeDefaultRefModel", 
    "nc.ui.bd.ref.busi.CalBodyDefaultRefModel", 
    "nc.ui.bd.ref.busi.OperatorDefaultRefModel", 
    "nc.ui.bd.ref.busi.OperatorDefaultRefModel", 
    "nc.ui.bd.ref.busi.ProduceDefaultRefModel", 
    "nc.ui.bd.ref.busi.PurorgDefaultRefModel", 
    "nc.ui.bd.ref.busi.PubTimeControlRefModel", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.AccidDefaultRefModel", 
    "nc.ui.bd.ref.busi.CostSubj_ArapDefaultRefModel", 
    "nc.ui.bd.ref.busi.CostSubj_ArapDefaultRefModel", 
    "nc.ui.bd.ref.busi.CustmandocUnitDefaultRefModel", 
    "nc.ui.bd.ref.busi.CorpDefaultRefModel", 
    "nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel", 
    "nc.ui.bd.ref.busi.SaleStruDefaultRefModel", 
    "nc.ui.bd.ref.busi.CashflowDefaultRefModel", 
    "nc.ui.bd.ref.busi.DefdoclistDefaultRefModel", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.DefaultRefModel_ARAP", 
    "nc.ui.bd.ref.busi.AccidDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccidDefaultRefModel", 
    "nc.ui.bd.ref.busi.PsndocDefaulRefModel", 
    "nc.ui.bd.ref.DefaultRefModel_multiLang", 
    "nc.ui.bd.ref.busi.ProdLineDefaultRefModel", 
    "nc.ui.bd.ref.busi.CorpKindDefaultRefModel", 
    "nc.ui.bd.ref.busi.SettleUnitDefaultRefModel", 
    "nc.ui.bd.ref.busi.AddressDefaultRefModel", 
    "nc.ui.bd.ref.busi.SettleCenterDefaultRefModel", 
    "nc.ui.bd.ref.busi.UserGroupDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccPeriodSchemeDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccsubjSchemeDefaultRefModel", 
    "nc.ui.bd.ref.busi.GlbookDefaultRefModel", 
    "nc.ui.bd.ref.busi.GlOrgDefaulRefModel", 
    "nc.ui.bd.ref.busi.GlorgbookDefaultRefModel", "", "", "", 
    "nc.ui.bd.ref.busi.DeptdocDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccsubjDefaultRefModel", 
    "nc.ui.bd.ref.busi.AccsubjDefaultRefModel", 
    "nc.ui.bd.ref.busi.GlbookorgDefaultRefModel", 
    "nc.ui.bd.ref.busi.SettleUnitDefaultGridTreeModel", 
    "nc.ui.bd.ref.busi.AccidDefaultRefModel", 
    "nc.ui.bd.ref.busi.GlbookDefaultRefModel", 
    "nc.ui.bd.ref.busi.GlbookcorpDefaultRefModel", 
    "nc.ui.bd.ref.busi.RoleDefaultModel", 
    "nc.ui.bd.ref.busi.StorDocMultiCorpDefaultRefModel", 
    "nc.ui.bd.ref.busi.CalBodyMultiCorpDefaultRefModel", 
    "nc.ui.ref.OrderRefModel",
    "nc.ui.mm.pub.pub1010.BomVerRefModel"};
}