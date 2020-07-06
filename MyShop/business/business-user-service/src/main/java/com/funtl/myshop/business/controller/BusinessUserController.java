package com.funtl.myshop.business.controller;

import com.funtl.myshop.provider.domain.TbUser;
import com.funtl.myshop.provider.service.api.TbUserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "user")
public class BusinessUserController {

    @Reference(version = "1.0.0")
    private TbUserService tbUserService;

    @GetMapping(value = "list")
    public List<TbUser> list() {
        List<TbUser> tbUsers = tbUserService.selectAll();
        return tbUsers;
    }
}
