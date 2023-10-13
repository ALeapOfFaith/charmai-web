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
 * @date 2023-07-29 10:24:33
 */
@Data
@TableName("wx_user_invite_code")
public class WxUserInviteCodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 微信用户id
	 */
	private String wxUserId;
	/**
	 * 邀请码
	 */
	private String inviteCode;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 逻辑删除标记（0：显示；1：隐藏）
	 */
	private String delFlag;

}
