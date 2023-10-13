package com.charmai.miniapp.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.ProductsEntity;
import com.charmai.miniapp.service.impl.PageUtils;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-31 23:24:39
 */
public interface ProductsService extends IService<ProductsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取所有的商品信息
     * @return
     */
    List<ProductsEntity> getAllProducts();

    /**
     * 检查商品是否存在
     */
    void checkProduct(String productId);
}

