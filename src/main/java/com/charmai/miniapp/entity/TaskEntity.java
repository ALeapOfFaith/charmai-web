package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 *
 */
@Data
@TableName("task")
public class TaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 任务所在GPU机器
	 */
	private String gpuAddress;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 任务状态 0 等待中，1 训练中，2训练完成
	 */
	private Integer status;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 创建时间
	 */
	private Date creatTime;

}
