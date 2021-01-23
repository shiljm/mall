package com.henau.mall.order.dao;

import com.henau.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 19:27:33
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
