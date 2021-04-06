package com.henau.mall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.henau.common.utils.HttpUtils;
import com.henau.common.utils.R;
import com.henau.mall.auth.feign.MemberFeignService;
import com.henau.mall.auth.vo.MemberRespVo;
import com.henau.mall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 处理社交登录请求
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    MemberFeignService memberFeignService;

    /**
     * 社交登录成功回调
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/oauth2.0/gitee/success")
    public String weibo(@RequestParam("code") String code) throws Exception {
        Map<String, String> header = new HashMap<>();
        Map<String, String> query = new HashMap<>();

        Map<String, String> map = new HashMap<>();
        map.put("client_id", "b3ca9154b7c08fe71b38cee97785e72b9aca290c7166b9b3b3d77b87d5c3119a");
        map.put("client_secret", "6b266460f17e991e1e292590ff0c05202303c9ff16c6694d1fa3ee94472b00e8");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.henaumall.com/oauth2.0/gitee/success");
        map.put("code", code);
        //1、根据code换取accessToken;
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", header, null, map);

        //2、处理
        if(response.getStatusLine().getStatusCode() == 200) {
            //获取到了accessToken
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //知道当前是那个社交用户
            //1、当前用户如果是第一次进入这个网站，自动注册进来（为当前社交用户生成一个会员信息账号，以后这个社交账号就对应指定的会员）
            //登录或者注册这个社交用户

            R oauthlogin = memberFeignService.oauthlogin(socialUser);
            if (oauthlogin.getCode() == 0){
                MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登录成功：用户：{}",data.toString());
                //2、登陆成功就跳回首页
                return "redirect:http://henaumall.com";
            }else {
                return "redirect:http://auth.henaumall.com/login.html";
            }

        }else {
            return "redirect:http://auth.henaumall.com/login.html";
        }
    }
}
