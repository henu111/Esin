package com.henu.sell.controller;

import com.henu.sell.Enums.ResultEnum;
import com.henu.sell.dto.OrderDTO;
import com.henu.sell.exception.SellException;
import com.henu.sell.service.OrderService;
import com.henu.sell.service.PayService;

import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    public void setOrderService(OrderService orderService){this.orderService=orderService;}
    private OrderService orderService;

    @Autowired
    public void setPayService(PayService payService){this.payService=payService;}
    private PayService payService;

    @GetMapping("create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        //1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2. 发起支付
        PayResponse payResponse = payService.create(orderDTO);

        //包含的对象 payRequest.setOpenid(orderDTO.getBuyerOpenid());
        //        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        //        payRequest.setOrderId(orderDTO.getOrderId());
        //        payRequest.setOrderName(ORDER_NAME);
        //        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        //把这些对象放到map中,再将map注入到pay/create的ftl文件中，实现动态注入。
        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);

        return new ModelAndView("pay/create", map);
    }

    /**
     * 微信异步通知
     * @param notifyData
     */
    @PostMapping("notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }
}
