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
 * @date 2023-07-29 11:13:39
 */
@Data
@TableName("wx_user_invite_code_record")
public class WxUserInviteCodeRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 邀请人id
	 */
	private String wxInviterUserId;
	/**
	 * 被邀请人id
	 */
	private String wxInviteeUserId;
	/**
	 * 
	 */
	private String inviteCode;
	/**
	 * 逻辑删除标记（0：显示；1：隐藏）
	 */
	private String delFlag;
	/**
	 * 
	 */
	private Date createTime;

}
