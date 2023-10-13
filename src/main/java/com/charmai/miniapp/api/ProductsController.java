package com.charmai.miniapp.api;

import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.ProductsEntity;
import com.charmai.miniapp.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-07-31-23:23
 * @Description:
 */
@RestController
@RequestMapping("/weixin/api")
public class ProductsController {


    @Autowired
    ProductsService productsService;


    @GetMapping("/products")
    @Operation(summary = "查询商品详情列表")
    public R<List<ProductsEntity>> getProducts() {
        List<ProductsEntity> productsEntities = productsService.getAllProducts();
        return R.ok(productsEntities);
    }

}
