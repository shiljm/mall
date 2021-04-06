package com.henau.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 17:15:28
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);

    List<BrandEntity> getBrandsByIds(List<Long> brandIds);
}

