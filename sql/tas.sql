-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
                         `id` bigint(20) NOT NULL COMMENT 'ID',
                         `user_id` varchar(56) NOT NULL COMMENT '用户ID',
                         `task_id` varchar(256) NULL DEFAULT NULL COMMENT '任务ID',
                         `status` bigint(4) NULL DEFAULT NULL COMMENT '任务状态',
                         `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                         `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                         `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                         `gpuAddress`varchar(256) NULL DEFAULT NULL COMMENT '服务器地址',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for gpu_node
-- ----------------------------
DROP TABLE IF EXISTS `gpu_node`;
CREATE TABLE `gpu_node`  (
                             `id` bigint(20) NOT NULL COMMENT 'ID',
                             `address` varchar(256) NOT NULL COMMENT '节点地址',
                             `status` int(4) NOT NULL COMMENT '节点状态',
                             `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `user_lora_model`;

CREATE TABLE `user_lora_model` (
                                   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '仅用于唯一标识记录，无其他用途',
                                   `user_id` varchar(45) NOT NULL COMMENT '用户id',
                                   `lora_name` varchar(45) NOT NULL COMMENT 'lora名称',
                                   `lora_url` varchar(128) NOT NULL COMMENT '数字分身的存储地址',
                                   `del_flag` tinyint(4) NOT NULL COMMENT '逻辑删除标识，del_flag=0表示该数字分身正在使用中',
                                   `create_time` datetime NOT NULL COMMENT '记录创建的时间',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数字分身';


-- ----------------------------
-- Table structure for model_template
-- ----------------------------
DROP TABLE IF EXISTS `model_template`;
CREATE TABLE `model_template`  (
                                   `template_id` bigint(20) NOT NULL COMMENT 'ID',
                                   `template_name` varchar(56) NOT NULL COMMENT '模板名称',
                                   `template_description` text NULL DEFAULT NULL COMMENT '模板描述',
                                   `template_mapping` bigint(20) NULL DEFAULT NULL COMMENT '',
                                   `del_flag` bigint(4) NULL COMMENT '删除标识',
                                   `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   `cover_url` varchar(256) NULL DEFAULT NULL COMMENT '小图',
                                   `pic_url` varchar(256) NULL DEFAULT NULL COMMENT '大图',
                                   PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;



