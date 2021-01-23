package com.henau.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.henau.mall.product.entity.BrandEntity;
import com.henau.mall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 1.引入oss-starter
 * 2.配置key，endpoint相关信息即可
 * 3.注入OSSClient
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Test
    public void contextLoads() {

//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为牛逼");

//        brandEntity.setName("中兴");
//
//        brandService.save(brandEntity);
//        System.out.println("保存成功...");

//        brandService.updateById(brandEntity);
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item) -> {
            System.out.println(item);
        });

    }

}
