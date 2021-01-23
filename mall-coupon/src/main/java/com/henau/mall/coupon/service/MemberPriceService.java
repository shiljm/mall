package com.henau.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 18:57:27
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

