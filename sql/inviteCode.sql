-- charmai.wx_user_invite_code definition

CREATE TABLE `wx_user_invite_code` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `wx_user_id` varchar(32) NOT NULL COMMENT '微信用户id',
                                       `invite_code` varchar(100) NOT NULL COMMENT '邀请码',
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `del_flag` char(2) DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                       PRIMARY KEY (`id`),
                                       KEY `wx_user_invite_codes_wx_user_id_IDX` (`wx_user_id`) USING BTREE,
                                       KEY `wx_user_invite_codes_invite_code_IDX` (`invite_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- charmai.wx_user_invite_code_record definition

CREATE TABLE `wx_user_invite_code_record` (
                                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                              `wx_inviter_user_id` varchar(32) DEFAULT NULL COMMENT '邀请人id',
                                              `wx_invitee_user_id` varchar(32) DEFAULT NULL COMMENT '被邀请人id',
                                              `invite_code` varchar(100) DEFAULT NULL,
                                              `del_flag` char(2) DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                              PRIMARY KEY (`id`),
                                              KEY `wx_user_invite_code_record_wx_invitee_user_id_IDX` (`wx_invitee_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;