package com.henau.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.henau.mall.product.dao.AttrGroupDao;
import com.henau.mall.product.dao.SkuSaleAttrValueDao;
import com.henau.mall.product.entity.BrandEntity;
import com.henau.mall.product.service.BrandService;
import com.henau.mall.product.service.SkuSaleAttrValueService;
import com.henau.mall.product.vo.SkuItemSaleAttrVo;
import com.henau.mall.product.vo.SkuItemVo;
import com.henau.mall.product.vo.SpuItemAttrGroupVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void test1() {
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(13L);
        System.out.println(saleAttrsBySpuId);
    }

    @Test
    public void test() {
        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(100L, 225L);
        System.out.println(attrGroupWithAttrsBySpuId);
    }

    @Test
    public void redisson() {
        System.out.println(redissonClient);
    }

    @Test
    public void testStringRedisTemplate() {
        //hello world
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        //保存
        ops.set("hello", "world" + UUID.randomUUID().toString());

        //查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据是：" + hello);
    }


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
