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
@TableName("model_template")
public class ModelTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long templateId;
	/**
	 * 模板名称
	 */
	private String templateName;
	/**
	 * 模板描述
	 */
	private String templateDescription;
	/**
	 * SD训练通用参数模板映射
	 */
	private String templateMapping;
	/**
	 * 删除标识
	 */
	private Long delFlag;
	/**
	 * 创建时间
	 */
	private Date creatTime;
	/**
	 * 小图
	 */
	private String coverUrl;
	/**
	 * 大图
	 */
	private String picUrl;
	/**
	 * 分类key
	 */
	private String typeCue;
	/**
	 * 使用次数
	 */
	private Integer used;

}
