package com.henu.sell.repository;

import com.henu.sell.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
  //List<类名> findBy对象名In（List<数据类型> 对象名List）
  //  声明    方法名(一个List类型的参数)
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
