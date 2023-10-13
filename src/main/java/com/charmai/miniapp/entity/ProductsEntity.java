package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-31 23:40:19
 */
@Data
@TableName("products")
public class ProductsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String productId;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 积分
	 */
	private Long point;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 逻辑删除标记（0：显示；1：隐藏）
	 */
	private String delFlag;

}
