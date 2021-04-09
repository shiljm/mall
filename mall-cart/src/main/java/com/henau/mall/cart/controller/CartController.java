package com.henau.mall.cart.controller;

import com.henau.common.constant.AuthServerConstant;
import com.henau.mall.cart.interceptor.CartInterceptor;
import com.henau.mall.cart.service.CartService;
import com.henau.mall.cart.vo.CartItem;
import com.henau.mall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /**
     *  浏览器有一个cookie; user-key; 标识用户的身份，有一个月的时间过期
     *  如果第一次使用jd的购物车功能，都会给一个临时的用户身份；
     *  浏览器以后保存，每次访问都会带上这个cookie
     *
     *  登录：session有
     *  没登录：按照cookie里面带来的user-key来做
     *  第一次：如果没有临时用户，帮忙创建一个临时用户。
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage() {

        //1、快速得到用户信息，id，user-key
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);

        return "cartList";
    }

    /**
     * 添加商品到购物车
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num, Model model) throws ExecutionException, InterruptedException {

        CartItem cartItem =  cartService.addToCart(skuId, num);

        model.addAttribute("item", cartItem);
        return "success";
    }
}