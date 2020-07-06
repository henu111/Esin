package com.henu.sell.service;

import com.henu.sell.dataobject.SellerInfo;


/**
 * 卖家端
 */
public interface SellerService {

    /**
     * 通过openid查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
    SellerInfo findSellerInfoByUsername(String username);
    SellerInfo findSellerInfoByPassword(String password);
}
