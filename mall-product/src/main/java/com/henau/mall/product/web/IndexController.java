package com.henau.mall.product.web;

import com.henau.mall.product.entity.CategoryEntity;
import com.henau.mall.product.service.CategoryService;
import com.henau.mall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @GetMapping({"/", "/index.html"})
    public String IndexPage(Model model) {

        //TODO 1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        model.addAttribute("categorys", categoryEntities);

        return "index";
    }

    @ResponseBody
    @GetMapping("/index/json/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {

        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        //获取一把锁， 只要锁的名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");

        //加锁
//        lock.lock(); //阻塞式等待,默认加的锁都是30S时间。
        //1、锁的自动续期，如果业务超长，运行期间自动给锁续上新的30s。不用担心业务时间长，锁自动过期被删除;
        //加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30S以后自动删除。
        lock.lock(10, TimeUnit.SECONDS);//10秒自动解锁，自动解锁时间一定要大于业务的执行时间。
        //问题：在锁时间到了以后，我们不会自动续期。
        //如果我们传递了锁的超时时间，就发送给Redis执行脚本，进行占锁，默认超时时间就是我们指定的时间。
        //如果我们未指定锁的超时时间，就使用30*1000【lockWatchdogTimeout看门狗的默认时间】；
        //只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗的默认时间】
        //定时任务续期时间为加锁时间/3

        //最佳实战
        //1、lock.lock(10, TimeUnit.SECONDS);省掉了这个续期操作。手动解锁
        try {
            System.out.println("加锁成功，执行业务....." + Thread.currentThread().getId());
            Thread.sleep(30000);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //解锁  假设解锁代码没有运行，Redisson会不会出现死锁
            lock.unlock();
        }
        return "hello";
    }
}
