package com.funtl.myshop.provider.service.impl;

import com.funtl.myshop.provider.domain.TbUser;
import com.funtl.myshop.provider.mapper.TbUserMapper;
import com.funtl.myshop.provider.service.api.TbUserService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(version = "1.0.0")
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;


    @Override
    public List<TbUser> selectAll() {
        return tbUserMapper.selectAll();
    }
}
