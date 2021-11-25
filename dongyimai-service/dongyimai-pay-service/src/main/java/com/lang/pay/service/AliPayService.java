package com.lang.pay.service;

import java.util.Map;

public interface AliPayService {
    /**
     * 交易预创建接口，获取支付链接
     *
     * @param out_trade_no 支付单号（例如：20211009121211019）
     * @param total_amount 支付金额（单位元，88.99元）
     * @return
     */
    public Map createNative(String out_trade_no, double total_amount);

    /**
     * 根据支付单号查询支付状态
     *
     * @param out_trade_no
     * @return
     */
    public Map queryPayStatus(String out_trade_no);

    /**
     * 退款
     */

    public Map refund(String trade_no, double refund_amount, String out_request_no);
}
