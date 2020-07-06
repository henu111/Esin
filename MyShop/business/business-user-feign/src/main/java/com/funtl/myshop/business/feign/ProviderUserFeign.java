package com.funtl.myshop.business.feign;

import com.funtl.myshop.business.feign.fallback.ProviderUserFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "provider-user", fallback = ProviderUserFallback.class)
public interface ProviderUserFeign {

    @GetMapping(value = "list")
    String list();

    @GetMapping(value = "hi")
    String hi();
}
