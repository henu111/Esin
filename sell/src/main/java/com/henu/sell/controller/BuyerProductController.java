package com.henu.sell.controller;
//
//import com.henu.sell.VO.ProductInfoVO;
//import com.henu.sell.VO.ProductVO;
//import com.henu.sell.VO.ResultVO;
//import com.henu.sell.dataobject.ProductCategory;
//import com.henu.sell.dataobject.ProductInfo;
//import com.henu.sell.service.CategoryService;
//import com.henu.sell.service.ProductService;
//import com.henu.sell.utils.ResultVOUtil;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/buyer/product")
//public class BuyerProductController {
//    private ProductService productService;
//    @Autowired
//    public void setProductService(ProductService productService){
//        this.productService = productService;
//    }
//
//    private CategoryService categoryService;
//    @Autowired
//    public void setCategoryService(CategoryService categoryService){
//        this.categoryService=categoryService;
//    }
//    @GetMapping(value = "/list")
// //   @ResponseBody
//    public ResultVO list(){
//        //查询所有商品
//            List<ProductInfo> productInfoList = productService.findUpAll();
//        //查询类目（一次性查询）
//       /*
//       //传统方法for遍历
//       List<Integer> categoryTypeList=new ArrayList<>();
//        for (ProductInfo productInfo:productInfoVOList){
//            categoryTypeList.add(productInfo.getCategoryType());
//        }*/
//       //lambda表达式
//        List<Integer> categoryTypeList= productInfoList.stream().map(e->e.getCategoryType()).collect(Collectors.toList());
//        List<ProductCategory> productCategoryList= categoryService.findByCategoryTypeIn(categoryTypeList);
//       //数据拼装
//        List<ProductVO> productVOList = new ArrayList<>();
//        for (ProductCategory productCategory:productCategoryList){
//            ProductVO productVO=new ProductVO();
//            productVO.setCategoryName(productCategory.getCategoryName());
//            productVO.setCategoryType(productCategory.getCategoryType());
//
//            List<ProductInfoVO> productInfoVOList=new ArrayList<>();
//            for(ProductInfo productInfo:productInfoList){
//                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
//                    ProductInfoVO productInfoVO =new ProductInfoVO();
//                    // BeanUtils.拷贝方法，替换大量的set代码
//                    BeanUtils.copyProperties(productInfo,productInfoVO);
//                    productInfoVOList.add(productInfoVO);
//
//                }
//            }
//            productVO.setProductInfoVOList(productInfoVOList);
//            productVOList.add(productVO);
//
//        }
//
//
//        return ResultVOUtil.success(productVOList);
//    }
//}



import com.henu.sell.VO.ProductInfoVO;
import com.henu.sell.VO.ProductVO;
import com.henu.sell.VO.ResultVO;
import com.henu.sell.dataobject.ProductCategory;
import com.henu.sell.dataobject.ProductInfo;
import com.henu.sell.service.CategoryService;
import com.henu.sell.service.ProductService;
import com.henu.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    public void setProductService(ProductService productService){this.productService=productService;}
    private ProductService productService;

    @Autowired
    public void setCategoryService(CategoryService categoryService){this.categoryService=categoryService;}
    private CategoryService categoryService;

    @GetMapping("/list")
//    @Cacheable(cacheNames = "product", key = "#sellerId", condition = "#sellerId.length() > 3", unless = "#result.getCode() != 0")
    public ResultVO list(@RequestParam(value = "sellerId", required = false) String sellerId) {
        //1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //2. 查询类目(一次性查询)
//        List<Integer> categoryTypeList = new ArrayList<>();
        //传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
        //精简方法(java8, lambda)
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        //3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo: productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
