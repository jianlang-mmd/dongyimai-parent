package com.lang.order.feign;

import com.lang.entity.PageResult;
import com.lang.entity.Result;
import com.lang.order.pojo.Order;
import com.lang.pay.pojo.PayLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(name = "dym-order")
@RequestMapping("/order")
public interface OrderFeign {
    /**
     * 修改订单状态
     *
     * @param out_trade_no
     * @param transaction_id
     * @return
     */
    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.GET)
    public Result updateOrderStatus(
            @RequestParam(value = "out_trade_no") String out_trade_no,
            @RequestParam(value = "transaction_id") String transaction_id
    );

    /***
     * Order分页条件搜索实现
     * @param order
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    Result<PageResult<Order>> findPage(@RequestBody(required = false) Order order, @PathVariable int page, @PathVariable int size);

    /***
     * Order分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    Result<PageResult<Order>> findPage(@PathVariable int page, @PathVariable int size);

    /***
     * 多条件搜索品牌数据
     * @param order
     * @return
     */
    @PostMapping("/search")
    Result<List<Order>> findList(@RequestBody(required = false) Order order);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    Result delete(@PathVariable Long id);

    /***
     * 修改Order数据
     * @param order
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    Result update(@RequestBody Order order, @PathVariable Long id);

    /***
     * 新增Order数据
     * @param order
     * @return
     */
    @PostMapping
    Result add(@RequestBody Order order);

    /***
     * 根据ID查询Order数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Order> findById(@PathVariable Long id);

    /***
     * 查询Order全部数据
     * @return
     */
    @GetMapping
    Result<List<Order>> findAll();

    /**
     * 从redis中查询用户的支付日志
     *
     * @return
     */
    @GetMapping("/searchPayLogFromRedis/{username}")
    public Result<PayLog> searchPayLogFromRedis(@PathVariable(name = "username") String username);
}