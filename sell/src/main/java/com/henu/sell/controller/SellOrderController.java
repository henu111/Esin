package com.henu.sell.controller;


import com.henu.sell.dto.OrderDTO;
import com.henu.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/order")
@Slf4j
public class SellOrderController{

    private OrderService orderService;
    @Autowired
    public void setOrderService(OrderService orderService){this.orderService=orderService;}
    @GetMapping("/list")
    public ModelAndView list(@RequestParam("page" ) Integer page,
                             @RequestParam("size" ) Integer size,
                             Map<String,Object>map){

        PageRequest pageRequest=PageRequest.of(page,size);

        Page<OrderDTO> orderDTOPage=orderService.findList(pageRequest);
        map.put("orderDTOPage",orderDTOPage);
        return new ModelAndView("/order/list",map);
    }

}