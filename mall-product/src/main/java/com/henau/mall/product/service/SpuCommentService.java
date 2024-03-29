package com.henau.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 16:46:36
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

