package com.henu.sell.controller;

import com.henu.sell.Enums.ResultEnum;
import com.henu.sell.config.ProjectUrlConfig;
import com.henu.sell.constant.CookieConstant;
import com.henu.sell.constant.RedisConstant;
import com.henu.sell.dataobject.SellerInfo;
import com.henu.sell.service.SellerService;
import com.henu.sell.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户登录登出及Redis获取
 */
@Controller
@RequestMapping("seller")
public class SellerUserController {

    @Autowired
    public void setSellerService(SellerService sellerService){this.sellerService=sellerService;}
    private SellerService sellerService;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate){this.redisTemplate=redisTemplate;}
    private StringRedisTemplate redisTemplate;

    @Autowired
    public void setProjectUrlConfig(ProjectUrlConfig projectUrlConfig){this.projectUrlConfig=projectUrlConfig;}
    private ProjectUrlConfig projectUrlConfig;

    @PostMapping("login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpServletResponse response,
                              Map<String, Object> map) {

        //1. openid去和数据库里的数据匹配   因为微信扫码登录需要资质，这里用作账户
        //SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
/*        SellerInfo sellerInfo = sellerService.findSellerInfoByUsername(username);
        if (sellerInfo == null) {
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/sell/seller/login/login");
            return new ModelAndView("common/error");
        }else {
            SellerInfo sellerInfo1=sellerService.findSellerInfoByPassword(password);
            if (sellerInfo1==null){
                map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
                map.put("url", "/sell/seller/login/login");
                return new ModelAndView("common/error");
            }
        }*/

        //2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), username, expire, TimeUnit.SECONDS);

        //3. 设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);

        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");

    }

    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, Object> map) {
        //1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2. 清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));

            //3. 清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }

        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }
}
