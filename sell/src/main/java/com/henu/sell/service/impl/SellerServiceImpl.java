package com.henu.sell.service.impl;

import com.henu.sell.dataobject.SellerInfo;
import com.henu.sell.repository.SellerInfoRepository;
import com.henu.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    public void setSellerInfoRepository(SellerInfoRepository repository){this.repository=repository;}
    private SellerInfoRepository repository;


    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }

    @Override
    public SellerInfo findSellerInfoByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public SellerInfo findSellerInfoByPassword(String password) {
        return repository.findByPassword(password);
    }
}
