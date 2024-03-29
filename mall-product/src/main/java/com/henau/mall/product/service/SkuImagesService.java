package com.henau.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 16:46:37
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}

