package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-06 15:10:27
 */
@Data
@TableName("model_lora")
public class ModelLoraEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 名称
	 */
	private String loraName;
	/**
	 * 权重
	 */
	private Double loraWeight;
	/**
	 * 关联模板ID
	 */
	private Long templateId;
	/**
	 * 删除标识
	 */
	private Long delFlag;
	/**
	 * 创建时间
	 */
	private Date creatTime;

}
