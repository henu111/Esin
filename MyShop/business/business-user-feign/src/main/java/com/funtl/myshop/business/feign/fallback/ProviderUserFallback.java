package com.funtl.myshop.business.feign.fallback;

import com.funtl.myshop.business.feign.ProviderUserFeign;
import org.springframework.stereotype.Component;

@Component
public class ProviderUserFallback implements ProviderUserFeign {
    @Override
    public String list() {
        return "请求失败，请检查您的网络";
    }

    @Override
    public String hi() {
        return null;
    }
}
