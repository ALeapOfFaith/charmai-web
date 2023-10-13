-- charmai.wx_user_points definition

CREATE TABLE `wx_user_points` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `wx_user_id` varchar(32) NOT NULL COMMENT '微信用户id',
                                  `total_points` bigint(20) DEFAULT '0' COMMENT '总积分',
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                  `del_flag` varchar(100) DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                  PRIMARY KEY (`id`),
                                  KEY `wx_user_points_wx_user_id_IDX` (`wx_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `wx_user_points_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wx_user_id` varchar(32) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '积分类型（0：增加；1：扣除）',
  `point` bigint(20) DEFAULT NULL COMMENT '积分详情',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `del_flag` char(2) DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `out_trade_no` varchar(100) DEFAULT NULL COMMENT '支付订单号',
  `increase_type` int(11) DEFAULT NULL COMMENT '增加积分类型（0：充值支付；1.分享）',
  PRIMARY KEY (`id`),
  KEY `wx_user_points_detail_wx_user_id_IDX` (`wx_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;