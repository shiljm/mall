package com.henau.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.henau.common.utils.PageUtils;
import com.henau.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author lijm
 * @email 13243030406@163.com
 * @date 2021-01-12 19:27:33
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

