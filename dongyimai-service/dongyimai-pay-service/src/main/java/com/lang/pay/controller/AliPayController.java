package com.lang.pay.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.order.feign.OrderFeign;
import com.lang.pay.pojo.PayLog;
import com.lang.pay.service.AliPayService;
import com.lang.util.IdWorker;
import com.lang.util.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class AliPayController {

    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 交易预创建接口，获取支付链接
     *
     * @return
     */
    @GetMapping("/creatNative")
    public Map creatNative() {
        String user_name = tokenDecode.getUserInfo().get("user_name");
        Result<PayLog> result = orderFeign.searchPayLogFromRedis(user_name);
        PayLog payLog = result.getData();
        if (payLog == null) {
            Map<Object, Object> map = new HashMap<>();
            map.put("msg", "支付出错");
        }
        //金额转换，支付日志中的金额是分，需要转成元
        String outTradeNo = payLog.getOutTradeNo();
        long totalFee = payLog.getTotalFee().longValue();
        double total_amount = totalFee / 100.00;
        // 返回值
        /*{
            "msg": "Success",
            "out_trade_no": "1446746379128897536",
            "code": "10000",
            "total_amount": 35,
            "qr_code": "https://qr.alipay.com/bax075575mrp1axynrhl0011"
        }*/

        //IdWorker idWorker = new IdWorker();
        //String out_trade_no = idWorker.nextId() + "";//为了测试，随机生成的支付单号
        //total_amount, qr_code ,out_trade_no, code msg
        Map map = aliPayService.createNative(outTradeNo, total_amount);

        return map;
    }

    /**
     * 检测支付状态，直到支付成功或者异常退出
     *
     * @param out_trade_no
     * @return
     */
    @GetMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {

        int count = 0; // 检测的次数,5分钟完成支付
        while (true) {
            Map map = aliPayService.queryPayStatus(out_trade_no);
            String trade_status = (String) map.get("trade_status");

            if (trade_status != null && trade_status.equals("TRADE_SUCCESS")) {
                //支付成功要做的事
                orderFeign.updateOrderStatus(out_trade_no, map.get("trade_no") + "");
                return new Result(true, StatusCode.OK, "支付成功");
            } else if (trade_status != null && trade_status.equals("TRADE_FINISHED")) {
                return new Result(true, StatusCode.OK, "交易结束");
            } else if (trade_status != null && trade_status.equals("TRADE_CLOSED")) {
                return new Result(true, StatusCode.OK, "未付款交易超时关闭");
            }

            //每隔3秒检测一次支付状态
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为3分钟
            count++;

            if (count >= 36) {
                return new Result(false, StatusCode.ERROR, "超过3分钟未支付");
            }
        }
    }

    @GetMapping("/refund/{trade_no}/{refund_amount}/{out_request_no}")
    public Map refund(@PathVariable("trade_no") String trade_no,
                      @PathVariable("refund_amount") double refund_amount,
                      @PathVariable("out_request_no") String out_request_no) {
        return aliPayService.refund(trade_no, refund_amount, out_request_no);
    }


}
