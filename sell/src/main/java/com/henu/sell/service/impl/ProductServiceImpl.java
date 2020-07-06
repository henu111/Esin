package com.henu.sell.service.impl;

import com.henu.sell.Enums.ProductStatusEnum;
import com.henu.sell.Enums.ResultEnum;
import com.henu.sell.dataobject.ProductInfo;
import com.henu.sell.dto.CartDTO;
import com.henu.sell.exception.SellException;
import com.henu.sell.repository.ProductInfoRepository;
import com.henu.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;
@Service
public class  ProductServiceImpl implements ProductService {


    private  ProductInfoRepository repository;
    @Autowired
    public void setProductInfoRepository (ProductInfoRepository repository) {
        this.repository = repository;
    }
    @Override
    public ProductInfo findOne(String productId)     {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable)
    {
        return repository.findAll((org.springframework.data.domain.Pageable) pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return null;
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList)
    {
        //直接将数量加到list上
        for (CartDTO cartDTO:cartDTOList)
        {
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if (productInfo == null)
            {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock = productInfo.getProductStock() + cartDTO.getProductQuantity();
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList)
    {

        //获取库存如果库存大于要减去的数量执行减操作
        for (CartDTO cartDTO:cartDTOList)
        {

            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if (productInfo==null)
            {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock = productInfo.getProductStock()-cartDTO.getProductQuantity();
            if (stock<0)
            {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            repository.save(productInfo);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatusEnum() == ProductStatusEnum.UP) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);
    }

    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatusEnum() == ProductStatusEnum.DOWN) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }
}
