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
 * @date 2023-08-06 12:36:58
 */
@Data
@TableName("model_template_type")
public class ModelTemplateTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long templateTypeId;
	/**
	 * 模板类别名称
	 */
	private String templateTypeName;
	/**
	 * 删除标识
	 */
	private Long delFlag;
	/**
	 * 创建时间
	 */
	private Date creatTime;
	/**
	 * 模板类别key
	 */
	private String templateTypeKey;

}
