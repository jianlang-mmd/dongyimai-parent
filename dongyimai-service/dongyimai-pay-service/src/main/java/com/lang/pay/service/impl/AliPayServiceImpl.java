package com.lang.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.lang.pay.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AliPayServiceImpl implements AliPayService {

    @Autowired
    private AlipayClient alipayClient;

    /**
     * 交易预创建接口，获取支付链接
     *
     * @param out_trade_no 支付单号（例如：20211009121211019）
     * @param total_amount 支付金额（单位元，88.99元）
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, double total_amount) {

        Map map = new HashMap();
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        request.setNotifyUrl("");//异步通知地址
        JSONObject bizContent = new JSONObject();

        bizContent.put("out_trade_no", out_trade_no);
        bizContent.put("total_amount", total_amount);
        bizContent.put("subject", "测试商品");
        bizContent.put("qr_code_timeout_express", "10m");

        request.setBizContent(bizContent.toString());//对象转成json字符串
        AlipayTradePrecreateResponse response = null;

        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess() && "10000".equals(response.getCode())) {
            String qrCode = response.getQrCode();
            map.put("qr_code", qrCode);
            map.put("out_trade_no", out_trade_no);
            map.put("code", response.getCode());//10000 调用成功
            map.put("msg", response.getMsg());
            map.put("total_amount", total_amount);
            System.out.println("预支付接口，调用成功");
        } else {
            map.put("qr_code", "");
            map.put("out_trade_no", "");
            map.put("code", response.getCode());//10000 调用成功
            map.put("msg", response.getMsg());
            map.put("total_amount", 0);
            System.out.println("预支付接口，调用失败");
        }

        return map;
    }

    /**
     * 根据支付单号查询支付状态
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {


        Map map = new HashMap();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", out_trade_no);
        bizContent.put("trade_no", "");

        request.setBizContent(bizContent.toString());//对象转成json字符串

        AlipayTradeQueryResponse response = null;

        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess() && "10000".equals(response.getCode())) {
            String code = response.getCode();//10000
            //            交易状态：
//            WAIT_BUYER_PAY（交易创建，等待买家付款）、
//            TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
//            TRADE_SUCCESS（交易支付成功）、
//            TRADE_FINISHED（交易结束，不可退款）
            String tradeStatus = response.getTradeStatus();

            map.put("code", code);
            map.put("trade_status", tradeStatus);
            //支付成功后，支付宝返回的一个交易流水号，存储在支付日志中的transaction_id字段
            String tradeNo = response.getTradeNo();
            map.put("trade_no", tradeNo);
            System.out.println("调用成功！");
        } else {
            System.out.println("调用失败");
            map.put("code", response.getCode());
            map.put("trade_status", response.getTradeStatus());
        }

        return map;
    }

    /**
     * 退款
     *
     * @param trade_no
     * @param refund_amount
     * @param out_request_no
     * @return
     */
    @Override
    public Map refund(String trade_no, double refund_amount, String out_request_no) {
        Map map = new HashMap();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();

        bizContent.put("trade_no", trade_no);
        bizContent.put("refund_amount", refund_amount);
        bizContent.put("out_request_no", out_request_no);

        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
            String code = response.getCode();
            String msg = response.getMsg();

            map.put("code", code);
            map.put("msg", msg);

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return map;
    }
}
