package com.henau.mall.member.dao;

import com.henau.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 19:11:10
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
