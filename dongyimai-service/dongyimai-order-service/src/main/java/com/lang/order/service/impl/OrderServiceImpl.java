package com.lang.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.entity.Result;
import com.lang.order.dao.OrderItemMapper;
import com.lang.order.dao.OrderMapper;
import com.lang.order.dao.PayLogMapper;
import com.lang.order.pojo.Order;
import com.lang.order.pojo.OrderItem;
import com.lang.order.service.OrderService;
import com.lang.entity.PageResult;
import com.lang.order.util.Cart;
import com.lang.pay.pojo.PayLog;
import com.lang.sellergoods.feign.ItemFeign;
import com.lang.sellergoods.pojo.Item;
import com.lang.user.feign.UserFeign;
import com.lang.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Order业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    //注入redis操作类
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ItemFeign itemFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    PayLogMapper payLogMapper;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public void updateOrderStatus(String out_trade_no, String transactionid) {
        /**
         * 修改订单状态
         *
         * @param out_trade_no   支付订单号
         * @param transaction_id 支付宝返回的交易流水号
         */
        //1.修改支付日志状态
        PayLog payLog = payLogMapper.selectById(out_trade_no);
        payLog.setPayTime(new Date());
        payLog.setTradeState("1");//已支付
        payLog.setTransactionId(transactionid);//交易号
        payLogMapper.updateById(payLog);

        //2.修改订单状态
        String orderList = payLog.getOrderList();//获取订单号列表
        String[] orderIds = orderList.split(",");//获取订单号数组

        for (String orderId : orderIds) {
            Order order = orderMapper.selectById(Long.parseLong(orderId));
            if (order != null) {
                order.setStatus("2");//已付款
                order.setPaymentTime(new Date());
                orderMapper.updateById(order);
            }
        }
        //3、清除redis缓存数据
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }

    /**
     * 根据用户查询payLog
     *
     * @param userId
     * @return
     */
    @Override
    public PayLog searchPayFromRedis(String userId) {

        return (PayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    /**
     * Order条件+分页查询
     *
     * @param order 查询条件
     * @param page  页码
     * @param size  页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Order> findPage(Order order, int page, int size) {
        Page<Order> mypage = new Page<>(page, size);
        QueryWrapper<Order> queryWrapper = this.createQueryWrapper(order);
        IPage<Order> iPage = this.page(mypage, queryWrapper);
        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Order分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Order> findPage(int page, int size) {
        Page<Order> mypage = new Page<>(page, size);
        IPage<Order> iPage = this.page(mypage, new QueryWrapper<Order>());

        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Order条件查询
     *
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order) {
        //构建查询条件
        QueryWrapper<Order> queryWrapper = this.createQueryWrapper(order);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Order构建查询对象
     *
     * @param order
     * @return
     */
    public QueryWrapper<Order> createQueryWrapper(Order order) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (order != null) {
            // 订单id
            if (!StringUtils.isEmpty(order.getOrderId())) {
                queryWrapper.eq("order_id", order.getOrderId());
            }
            // 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
            if (!StringUtils.isEmpty(order.getPayment())) {
                queryWrapper.eq("payment", order.getPayment());
            }
            // 支付类型，1、在线支付，2、货到付款
            if (!StringUtils.isEmpty(order.getPaymentType())) {
                queryWrapper.eq("payment_type", order.getPaymentType());
            }
            // 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
            if (!StringUtils.isEmpty(order.getPostFee())) {
                queryWrapper.eq("post_fee", order.getPostFee());
            }
            // 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
            if (!StringUtils.isEmpty(order.getStatus())) {
                queryWrapper.eq("status", order.getStatus());
            }
            // 订单创建时间
            if (!StringUtils.isEmpty(order.getCreateTime())) {
                queryWrapper.eq("create_time", order.getCreateTime());
            }
            // 订单更新时间
            if (!StringUtils.isEmpty(order.getUpdateTime())) {
                queryWrapper.eq("update_time", order.getUpdateTime());
            }
            // 付款时间
            if (!StringUtils.isEmpty(order.getPaymentTime())) {
                queryWrapper.eq("payment_time", order.getPaymentTime());
            }
            // 发货时间
            if (!StringUtils.isEmpty(order.getConsignTime())) {
                queryWrapper.eq("consign_time", order.getConsignTime());
            }
            // 交易完成时间
            if (!StringUtils.isEmpty(order.getEndTime())) {
                queryWrapper.eq("end_time", order.getEndTime());
            }
            // 交易关闭时间
            if (!StringUtils.isEmpty(order.getCloseTime())) {
                queryWrapper.eq("close_time", order.getCloseTime());
            }
            // 物流名称
            if (!StringUtils.isEmpty(order.getShippingName())) {
                queryWrapper.eq("shipping_name", order.getShippingName());
            }
            // 物流单号
            if (!StringUtils.isEmpty(order.getShippingCode())) {
                queryWrapper.eq("shipping_code", order.getShippingCode());
            }
            // 用户id
            if (!StringUtils.isEmpty(order.getUserId())) {
                queryWrapper.eq("user_id", order.getUserId());
            }
            // 买家留言
            if (!StringUtils.isEmpty(order.getBuyerMessage())) {
                queryWrapper.eq("buyer_message", order.getBuyerMessage());
            }
            // 买家昵称
            if (!StringUtils.isEmpty(order.getBuyerNick())) {
                queryWrapper.eq("buyer_nick", order.getBuyerNick());
            }
            // 买家是否已经评价
            if (!StringUtils.isEmpty(order.getBuyerRate())) {
                queryWrapper.eq("buyer_rate", order.getBuyerRate());
            }
            // 收货人地区名称(省，市，县)街道
            if (!StringUtils.isEmpty(order.getReceiverAreaName())) {
                queryWrapper.eq("receiver_area_name", order.getReceiverAreaName());
            }
            // 收货人手机
            if (!StringUtils.isEmpty(order.getReceiverMobile())) {
                queryWrapper.eq("receiver_mobile", order.getReceiverMobile());
            }
            // 收货人邮编
            if (!StringUtils.isEmpty(order.getReceiverZipCode())) {
                queryWrapper.eq("receiver_zip_code", order.getReceiverZipCode());
            }
            // 收货人
            if (!StringUtils.isEmpty(order.getReceiver())) {
                queryWrapper.eq("receiver", order.getReceiver());
            }
            // 过期时间，定期清理
            if (!StringUtils.isEmpty(order.getExpire())) {
                queryWrapper.eq("expire", order.getExpire());
            }
            // 发票类型(普通发票，电子发票，增值税发票)
            if (!StringUtils.isEmpty(order.getInvoiceType())) {
                queryWrapper.eq("invoice_type", order.getInvoiceType());
            }
            // 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
            if (!StringUtils.isEmpty(order.getSourceType())) {
                queryWrapper.eq("source_type", order.getSourceType());
            }
            // 商家ID
            if (!StringUtils.isEmpty(order.getSellerId())) {
                queryWrapper.eq("seller_id", order.getSellerId());
            }
        }
        return queryWrapper;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        this.removeById(id);
    }

    /**
     * 修改Order
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        this.updateById(order);
    }

    /**
     * 增加Order
     *
     * @param map 创建订单+订单明细
     *            参数是order对象，用来接收前端的一些 参数，并不能表示订单
     */
    @Override
    public void add(Map map) {
        String username = map.get("user_id") + "";
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);

        double totalPayment = 0;//支付的总金额
        String orderList = "";

        for (Cart cart : cartList) {
            //创建订单
            long orderId = this.createOrder(map, cart);
            orderList += (orderId + "");
            //创建订单明细
            this.createOrderItems(orderId, cart);
        }

        //-》扣减库存（商品服务）
        itemFeign.decrCount(map.get("user_id") + "");

        //-》增加积分（用户服务）
        userFeign.addPoints(map.get("user_id") + "");

        long outTradeNo = idWorker.nextId();
        PayLog payLog = new PayLog();
        payLog.setOutTradeNo(outTradeNo + ""); //主键
        payLog.setCreateTime(new Date());

        payLog.setTotalFee(this.calTotalFee(cartList)); //总分
        payLog.setUserId(username);

        payLog.setTradeState("0");//未支付，支付成功后变为1
        payLog.setOrderList(orderList.substring(0, orderList.length() - 1));//记录订单号，例如： 123,234,345,
        payLog.setPayType(map.get("paymentType") + "");

        redisTemplate.boundHashOps("paylog").put(username, payLog);
        payLogMapper.insert(payLog);


        //-》购物车数据清空
        redisTemplate.boundHashOps("cartList").delete(map.get("user_id") + "");
    }

    private long calTotalFee(List<Cart> cartList) {
        long totalFee = 0;//分
        for (Cart cart : cartList) {
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                double v = orderItem.getTotalFee().doubleValue();//元
                totalFee += (long) (v * 100);//分
            }
        }
        return totalFee;
    }

    /**
     * 创建订单明细
     */
    private void createOrderItems(long orderId, Cart cart) {
        if (orderId > 0) {
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(orderId);

                // 判断库存数量
                Integer num = orderItem.getNum();
                Result<Item> result = itemFeign.findById(orderItem.getItemId());

                if (result != null && result.getData() != null) {

                    Item item = result.getData();
                    Integer rem_num = item.getNum();//剩余库存

                    if (rem_num < num) {
                        throw new RuntimeException("当前商品库存不足，商品id=" + item.getId());
                    }

                    orderItemMapper.insert(orderItem);
                }

            }
        }
    }

    /**
     * 创建订单
     *
     * @param map
     * @param cart
     * @return
     */
    private long createOrder(Map map, Cart cart) {
        Order order = new Order();

        long orderId = idWorker.nextId();//自己生成的订单主键

        order.setOrderId(orderId);//订单编号
        order.setPayment(this.calOrderPayment(cart));//当前订单的支付金额
        order.setPaymentType(map.get("paymentType") + "");// 1 wx  2 zfb
        order.setStatus("1");//订单状态  1，未支付
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

//            order.setPaymentType(); 付款时间 ，支付成功后修改该字段

        order.setUserId(map.get("user_id") + "");
        order.setReceiver(map.get("receiver") + "");
        order.setReceiverMobile(map.get("receiverMobile") + "");
        order.setReceiverAreaName(map.get("receiverAreaName") + "");

        order.setSellerId(cart.getSellerId());//商家id

        boolean bol = this.save(order);//创建订单

        return bol ? orderId : -1;
    }

    private BigDecimal calOrderPayment(Cart cart) {
        List<OrderItem> orderItemList = cart.getOrderItemList();
        double payment = 0;
        for (OrderItem orderItem : orderItemList) {
            payment += orderItem.getTotalFee().doubleValue();
        }
        return new BigDecimal(payment);
    }

    /**
     * 根据ID查询Order
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(Long id) {
        return this.getById(id);
    }

    /**
     * 查询Order全部数据
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return this.list(new QueryWrapper<Order>());
    }
}
