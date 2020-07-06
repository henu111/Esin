package com.henu.sell.service.impl;



import com.henu.sell.Enums.OrderStatusEnum;
import com.henu.sell.Enums.PayStatusEnum;
import com.henu.sell.Enums.ResultEnum;
import com.henu.sell.converter.OrderMaster2OrderDTOConverter;
import com.henu.sell.dataobject.OrderDetail;
import com.henu.sell.dataobject.OrderMaster;
import com.henu.sell.dataobject.ProductInfo;
import com.henu.sell.dto.CartDTO;
import com.henu.sell.dto.OrderDTO;
import com.henu.sell.exception.SellException;
import com.henu.sell.repository.OrderDetailRepository;
import com.henu.sell.repository.OrderMasterRepository;
import com.henu.sell.service.OrderService;
import com.henu.sell.service.PayService;
import com.henu.sell.service.ProductService;
import com.henu.sell.service.WebSocket;
import com.henu.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService
{

    private OrderDetailRepository orderDetailRepository;
    private ProductService productService;
    private OrderMasterRepository orderMasterRepository;
    private PayService payService;
    @Autowired
    public void setOrderDetailRepository( OrderDetailRepository orderDetailRepository){ this.orderDetailRepository=orderDetailRepository;}
    @Autowired
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }
    @Autowired
    public  void setOrderMasterRepository(OrderMasterRepository orderMasterRepository) { this.orderMasterRepository=orderMasterRepository; }
    @Autowired
    public void setPayService(PayService payService){this.payService=payService;}
    @Autowired
    public void setWebSocket(WebSocket webSocket){this.webSocket=webSocket;}
    private WebSocket webSocket;



    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO)
    {
        /*从数据库查询物品的存量，下单成功扣库存*/

        /*从数据库查询物品的单价，*/
        //初始时就创建orderId
        String orderId=KeyUtil.genUniqueKey();
        BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail:orderDTO.getOrderDetailList())
        {
            ProductInfo productInfo= productService.findOne(orderDetail.getProductId());//获取前端传入的商品Id
            if(productInfo == null)
            {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //获取库存
           // productInfoService.findOne(orderDetail.getProductId()).getProductStock();
          //不用减库前判断？
           List<CartDTO> cartDTOList = orderDTO.getOrderDetailList()
                   .stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity()))
                   .collect(Collectors.toList());
            productService.decreaseStock(cartDTOList);

            //发送websocket消息
            webSocket.onMessage("有新的订 单");
            /*计算总价*/
            //orderDetail.getProductQuantity()获取前端传入的购买商品的数量
            orderAmount= productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            /*写入数据库表orderDetail*/
            BeanUtils.copyProperties(productInfo,orderDetail);//spring提供的方法将一张表里的内容快速拷贝到另一张表中

            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);

            orderDetailRepository.save(orderDetail);
        }



       /*写入数据库表orderMaster、*/
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);

        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }


    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);

        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        //将数据封装到PageImpl中，返回
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage=orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        //将数据封装到PageImpl中，返回
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO)
    {

        /*取消订单加库存*/
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //如果已支付, 需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){

            payService.refund(orderDTO);
        }

        return orderDTO;

    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
