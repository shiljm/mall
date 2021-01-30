package com.henau.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.to.SkuReductionTo;
import com.henau.common.utils.PageUtils;
import com.henau.mall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 18:57:27
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo reductionTo);
}

