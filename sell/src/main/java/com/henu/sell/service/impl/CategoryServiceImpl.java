package com.henu.sell.service.impl;

import com.henu.sell.dataobject.ProductCategory;
import com.henu.sell.repository.ProductCategoryRepository;

import com.henu.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    private ProductCategoryRepository repository;
    @Autowired
    public void setProductCategoryRepository (ProductCategoryRepository repository) {
        this.repository = repository;
    }
    //卖家端查询方法findOne/findAll
    @Override
    public ProductCategory findOne(Integer categoryId) {
        return  repository.findById(categoryId).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    //买家端查询方法
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {

         return  repository.save(productCategory);
    }

}
