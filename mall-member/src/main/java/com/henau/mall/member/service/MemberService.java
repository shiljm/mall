package com.henau.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.member.entity.MemberEntity;
import com.henau.mall.member.exception.PhoneExisException;
import com.henau.mall.member.exception.UsernameExistException;
import com.henau.mall.member.vo.MemberLoginVo;
import com.henau.mall.member.vo.MemberRegistVo;
import com.henau.mall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 19:11:10
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExisException;

    void checkUsernameUnique(String username) throws UsernameExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser socialUser) throws Exception;
}

