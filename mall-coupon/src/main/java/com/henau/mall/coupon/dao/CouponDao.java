package com.henau.mall.coupon.dao;

import com.henau.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 18:57:27
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
