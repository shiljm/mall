package com.henau.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.henau.common.exception.BizCodeEnume;
import com.henau.mall.member.exception.PhoneExisException;
import com.henau.mall.member.exception.UsernameExistException;
import com.henau.mall.member.feign.CouponFeignService;
import com.henau.mall.member.vo.MemberLoginVo;
import com.henau.mall.member.vo.MemberRegistVo;
import com.henau.mall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.henau.mall.member.entity.MemberEntity;
import com.henau.mall.member.service.MemberService;
import com.henau.common.utils.PageUtils;
import com.henau.common.utils.R;



/**
 * 会员
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 19:11:10
 */
@RestController
@RequestMapping("/member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R membercoupons = couponFeignService.membercoupons();

        return R.ok().put("member",memberEntity).put("coupons",membercoupons.get("coupons"));
    }

    @PostMapping("/oauth/login")
    public R oauthlogin(@RequestBody SocialUser socialUser) throws Exception {

        MemberEntity entity = memberService.login(socialUser);
        if (entity!=null){
            //TODO 1、登录成功处理
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXVEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXVEPTION.getMsg());
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo) {

        MemberEntity entity = memberService.login(vo);
        if (entity!=null){
            //TODO 1、登录成功处理
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXVEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXVEPTION.getMsg());
        }
    }

    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo) {

        try {
            memberService.regist(vo);
        }catch (PhoneExisException e){
            return R.error(BizCodeEnume.PHONE_EXIST_EXVEPTION.getCode(), BizCodeEnume.PHONE_EXIST_EXVEPTION.getMsg());
        }catch (UsernameExistException e){
            return R.error(BizCodeEnume.USER_EXIST_EXVEPTION.getCode(), BizCodeEnume.USER_EXIST_EXVEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
