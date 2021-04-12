package com.henau.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.henau.common.utils.R;
import com.henau.mall.cart.feign.ProductFeignService;
import com.henau.mall.cart.interceptor.CartInterceptor;
import com.henau.mall.cart.service.CartService;
import com.henau.mall.cart.vo.Cart;
import com.henau.mall.cart.vo.CartItem;
import com.henau.mall.cart.vo.SkuInfoVo;
import com.henau.mall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    private final String CART_PREFIX = "henaumall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartIOps();

        String res = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(res)) {
            //购物车无此商品
            //2、商品新商品到添加到购物车
            CartItem cartItem = new CartItem();
            //1、远程查询当前要添加的商品的信息
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });

                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            }, executor);

            //3、远程查询sku组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);

            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValues).get();

            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), s);

            return cartItem;
        }else {
            //购物车有此商品，修改数量
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);

            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));

            return cartItem;
        }
    }

    /**
     * 获取到我们要操作的购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartIOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            //henaumall:cart:1
            cartKey = CART_PREFIX+userInfoTo.getUserId();
        }else {
            cartKey = CART_PREFIX+userInfoTo.getUserKey();
        }

        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }

    @Override
    public CartItem getCartItem(Long skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getCartIOps();

        String str = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(str, CartItem.class);
        return cartItem;
    }

    /**
     * 获取整个购物车
     *
     * @return
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //1、登录
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //2、如果临时购物车的数据还没有进行合并[合并购物车]
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItem = getCartItems(tempCartKey);
            if (tempCartItem != null) {
                //临时购物车有数据，需要合并
                for (CartItem item : tempCartItem) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                //清除临时购物车的数据
                clearCart(tempCartKey);
            }
            //3、获取登录后的购物车的数据[包含合并过来的临时购物车的数据，和登录后的购物车的数据]
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);


        }else {
            //2、没登录
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车的所有购物项
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }

        return cart;
    }

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (values != null && values.size() > 0) {
            List<CartItem> collect = values.stream().map(obj -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    public void clearCart(String cartkey) {
        redisTemplate.delete(cartkey);
    }

    /**
     * 删除购物项
     *
     * @param skuId
     */
    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartIOps = getCartIOps();
        cartIOps.delete(skuId.toString());
    }

    /**
     * 修改购物项数量
     *
     * @param skuId
     * @param num
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartIOps = getCartIOps();
        cartIOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    /**
     * 勾选购物项
     *
     * @param skuId
     * @param check
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartIOps = getCartIOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1?true:false);
        String s = JSON.toJSONString(cartItem);
        cartIOps.put(skuId.toString(), s);
    }
}
