package com.henau.mall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.henau.common.utils.HttpUtils;
import com.henau.mall.member.dao.MemberLevelDao;
import com.henau.mall.member.entity.MemberLevelEntity;
import com.henau.mall.member.exception.PhoneExisException;
import com.henau.mall.member.exception.UsernameExistException;
import com.henau.mall.member.vo.MemberLoginVo;
import com.henau.mall.member.vo.MemberRegistVo;
import com.henau.mall.member.vo.SocialUser;
import com.henau.mall.member.vo.SocialUser2;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.aspectj.weaver.patterns.HasMemberTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.henau.common.utils.PageUtils;
import com.henau.common.utils.Query;

import com.henau.mall.member.dao.MemberDao;
import com.henau.mall.member.entity.MemberEntity;
import com.henau.mall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();

        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        //检查用户名和手机号是否唯一.为了让controller能感知异常，异常机制
        checkPhoneUnique(vo.getPhone());
        checkUsernameUnique(vo.getUserName());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());

        entity.setNickname(vo.getUserName());

        //设置密码，密码要进行加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);

        //其他的默认信息

        //保存
        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExisException {
        MemberDao memberDao = this.baseMapper;
        Integer mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile > 0) {
            throw new PhoneExisException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException {
        MemberDao memberDao = this.baseMapper;
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0) {
            throw new UsernameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();

        //1.去数据库查询  SELECT * FROM `ums_member` WHERE username = ? OR mobile = ?
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct).or().eq("mobile", loginacct));
        if (entity == null){
            //登录失败
            return null;
        }else{
            //1、获取到数据库的password
            String passworDb = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //2、密码匹配
            boolean matches = passwordEncoder.matches(password, passworDb);
            if (matches){
                return entity;
            }else {
                return null;
            }
        }
    }

    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> query = new HashMap<>();
        String access_token = socialUser.getAccess_token();

        query.put("access_token", access_token);
        //登录和注册合并逻辑
        HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", headers, query);
        if (response.getStatusLine().getStatusCode() == 200) {
            String s = EntityUtils.toString(response.getEntity());
            SocialUser2 socialUser2 = JSON.parseObject(s, SocialUser2.class);
            long id = socialUser2.getId();
            MemberDao memberDao = this.baseMapper;
            MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", id));
            if (memberEntity != null) {
                //这个用户已经注册
                MemberEntity update = new MemberEntity();
                update.setId(memberEntity.getId());
                update.setAccessToken(socialUser.getAccess_token());
                update.setExpiresIn(socialUser.getExpires_in());

                memberDao.updateById(update);

                memberEntity.setNickname(socialUser2.getName());
                memberEntity.setAccessToken(socialUser.getAccess_token());
                memberEntity.setExpiresIn(socialUser.getExpires_in());
                return memberEntity;
            }else{
                //2、没有查到当前社交用户对应的记录我们就需要注册一个
                MemberEntity regist = new MemberEntity();
                try{
                    //3、查询当前社交用户的社交账号信息（昵称，性别）
                    regist.setNickname(socialUser2.getName());
                    regist.setEmail(socialUser2.getEmail());
                }finally {
                    regist.setSocialUid(String.valueOf(socialUser2.getId()));
                    regist.setAccessToken(socialUser.getAccess_token());
                    regist.setExpiresIn(socialUser.getExpires_in());

                    memberDao.insert(regist);
                    return regist;
                }
            }
        }
        return null;
    }
}