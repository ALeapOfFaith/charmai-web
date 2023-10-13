package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.entity.GpuNodeEntity;
import com.charmai.miniapp.entity.ProductsEntity;
import com.charmai.miniapp.mapper.ProductsMapper;
import com.charmai.miniapp.service.ProductsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;


@Service("productsService")
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, ProductsEntity> implements ProductsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductsEntity> page = this.page(
                new Query<ProductsEntity>().getPage(params),
                new QueryWrapper<ProductsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductsEntity> getAllProducts() {
        return baseMapper.selectList(new QueryWrapper<ProductsEntity>().eq("del_flag", 0));
    }

    @Override
    public void checkProduct(String productId) {
        ProductsEntity productsEntity = baseMapper.selectOne(new QueryWrapper<ProductsEntity>().eq("product_id", productId));
        if (ObjectUtils.isEmpty(productsEntity)) {
            throw new ServiceException(ExceptionConstant.PRODUCT_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.PRODUCT_NOT_EXIT_EXCEPTION_CODE);

        }
    }


}