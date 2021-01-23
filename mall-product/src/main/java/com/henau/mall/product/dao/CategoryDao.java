package com.henau.mall.product.dao;

import com.henau.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 17:15:28
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
