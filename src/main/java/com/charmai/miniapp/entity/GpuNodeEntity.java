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
 * @date 2023-07-26 18:19:34
 */
@Data
@TableName("gpu_node")
public class GpuNodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 节点地址
	 */
	private String address;
	/**
	 * 节点状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date creatTime;

}
