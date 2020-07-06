package com.henu.sell.repository;

import com.henu.sell.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openid);
    SellerInfo findByUsername(String username);
    SellerInfo findByPassword(String password);
}