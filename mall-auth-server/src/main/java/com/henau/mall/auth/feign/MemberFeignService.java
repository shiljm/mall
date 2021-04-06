package com.henau.mall.auth.feign;

import com.henau.common.utils.R;
import com.henau.mall.auth.vo.SocialUser;
import com.henau.mall.auth.vo.UserLoginVo;
import com.henau.mall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth/login")
    R oauthlogin(@RequestBody SocialUser socialUser) throws Exception;
}
