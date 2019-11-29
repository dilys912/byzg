package nc.ui.ic.pub.bill;

import nc.ui.scm.pub.bill.ScmButtonConst;

public abstract interface ICButtonConst extends ScmButtonConst
{
  public static final String BTN_BILL_ADD_MANUAL = "自制";
  public static final String BTN_BILL_ADD_DB_OUT = "调拨出库单";
  public static final String BTN_BILL_ADD_DB = "调拨订单";
  public static final String BTN_BILL_ADD_DB_DELTA = "三方调拨订单";
  public static final String BTN_BILL_ADD_DB_CORP = "公司间调拨订单";
  public static final String BTN_BILL_ADD_DB_INVORG = "组织间调拨订单";
  public static final String BTN_BILL_ADD_DB_ININVORG = "组织内调拨订单";
  public static final String BTN_BILL_RATIO_OUT = "配比出库";
  public static final String BTN_BILL_REF_MR = "参照物资需求申请单";
  public static final String BTN_BILL_BATCH_SAVE = "批保存";
  public static final String BTN_LINE_AUTO_FILL = "自动取数";
  public static final String BTN_LINE_SPACE = "货位";
  public static final String BTN_LINE_SERIAL = "序列号";
  public static final String BTN_LINE_BARCODE = "条形码";
  public static final String BTN_LINE_KD_CALCULATE = "扣吨计算";
  public static final String BTN_SIGN = "签字";
  public static final String BTN_EXECUTE_SIGN_CANCEL = "取消签字";
  public static final String BTN_EXECUTE_BARCODE_CLOSE = "条码关闭";
  public static final String BTN_EXECUTE_BARCODE_OPEN = "条码打开";
  public static final String BTN_PRINT_SPACE = "打印货位";
  public static final String BTN_ASSIST_FUNC_MANUAL_RETURN = "自制退库";
  public static final String BTN_ASSIST_FUNC_PO_RETURN = "采购订单退库";
  public static final String BTN_ASSIST_FUNC_WW_RETURN = "委外订单退库";
  public static final String BTN_ASSIST_FUNC_RETURN = "还回";
  public static final String BTN_ASSIST_FUNC_DISPENSE = "配套";
  public static final String BTN_ASSIST_FUNC_ONHAND = "存量显示/隐藏";
  public static final String BTN_ASSIST_FUNC_SCAN = "扫描录入";
  public static final String BTN_ASSIST_FUNC_INV_CHECK = "核对存货";
  public static final String BTN_ASSIST_FUNC_PICKUP_AUTO = "自动拣货";
  public static final String BTN_ASSIST_FUNC_REFER_IN = "参照入库单";
  public static final String BTN_ASSIST_FUNC_XCDG = "形成代管";
  public static final String BTN_ASSIST_FUNC_SETTLE_PATH = "指定结算路径";
  public static final String BTN_ASSIST_FUNC_CON_LOT = "批次指定";
  public static final String BTN_ASSIST_FUNC_CON_SPACE = "货位指定";
  public static final String BTN_ASSIST_FUNC_CON_CONVEY = "运输工具指定";
  public static final String BTN_ASSIST_FUNC_CAL_PACKAGE = "计算件数";
  public static final String BTN_ASSIST_FUNC_SUM = "数据汇总";
  public static final String BTN_ASSIST_FUNC_CORRECT = "调差";
  public static final String BTN_ASSIST_FUNC_CORRECT_CANCEL = "取消调差";
  public static final String BTN_ASSIST_FUNC_LOSSONWAY = "生成途损";
  public static final String BTN_ASSIST_QUERY_CREDIT = "联查客户信用";
  public static final String BTN_ASSIST_QUERY_LEND_BOOK = "联查借出备查簿";
  public static final String BTN_IMPORT_1ST_BARCODE = "导入主条码";
  public static final String BTN_IMPORT_2ND_BARCODE = "导入次条码";
  public static final String BTN_IMPORT_BOTH_BARCODE = "导入主次条码";
  public static final String BTN_IMPORT_SOURCE_BARCODE = "导入来源单据条码";
  public static final String BTN_PRINT_SUM_BATCH = "汇总打印批次";
  public static final String BTN_PRINT_CERTIFICATION = "打印质证书";
  public static final String BTN_INBILL_ONWAY = "在途入库单查询";
  public static final String BTN_BANLANCE_QUERY = "主辅计量平衡";
}