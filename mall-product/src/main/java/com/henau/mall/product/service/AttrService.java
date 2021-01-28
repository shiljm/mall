package com.henau.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.product.entity.AttrEntity;
import com.henau.mall.product.vo.AttrRespVo;
import com.henau.mall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 17:10:00
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attrVo);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);
}

