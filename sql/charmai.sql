/*
 Navicat Premium Data Transfer

 Source Server         : 111
 Source Server Type    : MySQL
 Source Server Version : 50742
 Source Host           : 47.98.117.149:3306
 Source Schema         : charmai

 Target Server Type    : MySQL
 Target Server Version : 50742
 File Encoding         : 65001

 Date: 05/08/2023 19:11:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album_save
-- ----------------------------
DROP TABLE IF EXISTS `album_save`;
CREATE TABLE `album_save`  (
                               `photo_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `user_id` varchar(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                               `photo_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片url',
                               `small_pic_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小图',
                               `photo_width` double NULL DEFAULT NULL COMMENT '宽',
                               `photo_height` double NULL DEFAULT NULL COMMENT '高',
                               `del_flag` bigint(4) NULL DEFAULT NULL COMMENT '删除标识',
                               `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`photo_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for album_upload
-- ----------------------------
DROP TABLE IF EXISTS `album_upload`;
CREATE TABLE `album_upload`  (
                                 `photo_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
                                 `photo_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片url',
                                 `small_pic_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小图',
                                 `photo_width` double NULL DEFAULT NULL COMMENT '宽',
                                 `photo_height` double NULL DEFAULT NULL COMMENT '高',
                                 `del_flag` int(4) NULL DEFAULT NULL COMMENT '是否删除（0删除 1不删除）',
                                 `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 PRIMARY KEY (`photo_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 418 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gpu_node
-- ----------------------------
DROP TABLE IF EXISTS `gpu_node`;
CREATE TABLE `gpu_node`  (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点地址',
                             `status` int(4) NOT NULL COMMENT '节点状态',
                             `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_lora
-- ----------------------------
DROP TABLE IF EXISTS `model_lora`;
CREATE TABLE `model_lora`  (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `lora_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                               `lora_weight` double NULL DEFAULT NULL COMMENT '权重',
                               `template_id` bigint(4) NULL DEFAULT NULL COMMENT '关联模板ID',
                               `del_flag` bigint(4) NULL DEFAULT NULL COMMENT '删除标识',
                               `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_template
-- ----------------------------
DROP TABLE IF EXISTS `model_template`;
CREATE TABLE `model_template`  (
                                   `template_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `template_name` varchar(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
                                   `template_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板描述',
                                   `template_mapping` varchar(256) NULL DEFAULT NULL COMMENT 'SD训练通用参数模板映射',
                                   `del_flag` bigint(4) NULL DEFAULT NULL COMMENT '删除标识',
                                   `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   `cover_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '小图',
                                   `pic_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '大图',
                                   `type_cue` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                   `used` int(10) NULL DEFAULT NULL COMMENT '使用次数',
                                   PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for photo_generate_task
-- ----------------------------
DROP TABLE IF EXISTS `photo_generate_task`;
CREATE TABLE `photo_generate_task`  (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `wx_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `task_status` int(11) NULL DEFAULT NULL,
                                        `template_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `create_time` datetime(0) NULL DEFAULT NULL,
                                        `start_time` datetime(0) NULL DEFAULT NULL,
                                        `end_time` datetime(0) NULL DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
                             `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
                             `point` bigint(20) NULL DEFAULT NULL COMMENT '积分',
                             `price` decimal(10, 2) NOT NULL COMMENT '价格',
                             `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
                             `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                             PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                         `user_id` varchar(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
                         `task_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务ID',
                         `status` int(4) NULL DEFAULT NULL COMMENT '任务状态',
                         `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                         `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                         `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                         `gpu_address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器地址',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_lora_model
-- ----------------------------
DROP TABLE IF EXISTS `user_lora_model`;
CREATE TABLE `user_lora_model`  (
                                    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '仅用于唯一标识记录，无其他用途',
                                    `user_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                    `lora_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'lora名称',
                                    `lora_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数字分身的存储地址',
                                    `del_flag` tinyint(4) NOT NULL COMMENT '逻辑删除标识，del_flag=0表示该数字分身正在使用中',
                                    `create_time` datetime(0) NOT NULL COMMENT '记录创建的时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字分身' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user`  (
                            `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键',
                            `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                            `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户备注',
                            `del_flag` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                            `app_type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用类型(1:小程序，2:公众号)',
                            `subscribe` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否订阅（1：是；0：否；2：网页授权用户）',
                            `subscribe_scene` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回用户关注的渠道来源，',
                            `subscribe_time` datetime(0) NULL DEFAULT NULL COMMENT '关注时间',
                            `subscribe_num` int(11) NULL DEFAULT NULL COMMENT '关注次数',
                            `cancel_subscribe_time` datetime(0) NULL DEFAULT NULL COMMENT '取消关注时间',
                            `open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户标识',
                            `nick_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
                            `sex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别（1：男，2：女，0：未知）',
                            `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在城市',
                            `country` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在国家',
                            `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在省份',
                            `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
                            `language` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户语言',
                            `headimg_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
                            `union_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'union_id',
                            `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户组',
                            `tagid_list` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签列表',
                            `qr_scene_str` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码扫码场景',
                            `latitude` double NULL DEFAULT NULL COMMENT '地理位置纬度',
                            `longitude` double NULL DEFAULT NULL COMMENT '地理位置经度',
                            `precision` double NULL DEFAULT NULL COMMENT '地理位置精度',
                            `session_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会话密钥',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `uk_openid`(`open_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user_invite_code
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_invite_code`;
CREATE TABLE `wx_user_invite_code`  (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `wx_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信用户id',
                                        `invite_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邀请码',
                                        `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `wx_user_invite_codes_wx_user_id_IDX`(`wx_user_id`) USING BTREE,
                                        INDEX `wx_user_invite_codes_invite_code_IDX`(`invite_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user_invite_code_record
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_invite_code_record`;
CREATE TABLE `wx_user_invite_code_record`  (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                               `wx_inviter_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人id',
                                               `wx_invitee_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被邀请人id',
                                               `invite_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                               `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                               `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
                                               PRIMARY KEY (`id`) USING BTREE,
                                               INDEX `wx_user_invite_code_record_wx_invitee_user_id_IDX`(`wx_invitee_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_pay_record`;
CREATE TABLE `wx_user_pay_record`  (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `wx_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `out_trade_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `type` int(11) NULL DEFAULT NULL COMMENT '1无邀请码支付2邀请码支付3积分支付',
                                       `pay_fee` decimal(10, 0) NULL DEFAULT NULL,
                                       `trade_state` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `invite_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `points` bigint(20) NULL DEFAULT NULL,
                                       `create_time` datetime(0) NULL DEFAULT NULL,
                                       `update_time` datetime(0) NULL DEFAULT NULL,
                                       `pay_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user_points
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_points`;
CREATE TABLE `wx_user_points`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `wx_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信用户id',
                                   `total_points` bigint(20) NULL DEFAULT 0 COMMENT '总积分',
                                   `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
                                   `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
                                   `del_flag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `wx_user_points_wx_user_id_IDX`(`wx_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user_points_detail
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_points_detail`;
CREATE TABLE `wx_user_points_detail`  (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `wx_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                          `type` int(11) NULL DEFAULT NULL COMMENT '积分类型（0：增加；1：扣除）',
                                          `point` bigint(20) NULL DEFAULT NULL COMMENT '积分详情',
                                          `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
                                          `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                                          `out_trade_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付订单号',
                                          `increase_type` int(11) NULL DEFAULT NULL COMMENT '增加积分类型（0：充值支付；1.分享）',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          INDEX `wx_user_points_detail_wx_user_id_IDX`(`wx_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO `model_lora` VALUES (1, 'dunhuang_20230730225620-000009', 0.7, 1, 0, '2023-08-05 18:57:42');
INSERT INTO `model_lora` VALUES (2, 'xindatang_20230802165322-000009', 0.8, 2, 0, '2023-08-05 18:57:42');
INSERT INTO `model_lora` VALUES (3, 'budala_20230803151036-000009', 0.7, 3, 0, '2023-08-05 18:57:42');
INSERT INTO `model_lora` VALUES (4, 'Miao girl costume_20230709152204', 0.6, 4, 0, '2023-08-05 19:00:14');
INSERT INTO `model_template` VALUES (1, '一梦入敦煌', '在金色的沙漠中，敦煌飞天彷佛悠然翩翩起舞。', 1, 0, NULL, 'https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_33864b7d-7ee9-4183-bb6c-018a6ec40c01.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_f86f5f12-c8d4-4064-8a4c-96b885ae078e.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_c80c665c-2e63-4237-b5bf-ec9886bbfa2c.png', NULL, 'default',21000);
INSERT INTO `model_template` VALUES (2, '华灯初上，盛世长安', '杯盏不停 笙歌不息 灯火不灭 这便是长安夜', 1, 0, NULL, 'https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_6afcd5ce-05bf-4c43-921c-24f806470fe9.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_25f3275f-c48b-4789-9ce1-dbd3024614a4.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_1afa2d31-2628-4dfa-a6a4-7b2445382f15.png', NULL, 'default',22000);
INSERT INTO `model_template` VALUES (3, '布达拉宫', '亲爱的旅人路过了布达拉宫。少女的祈愿随风到达了神明跟前', 1, 0, NULL, 'https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_24a25aba-e953-4f2c-9c3e-c9f504622bbc.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_7503d882-e2aa-45fa-aa0c-97b35a1e9c70.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_d9d54b40-8175-4ab3-8ad7-ab08ce1087e7.png', NULL, 'default',18000);
INSERT INTO `model_template` VALUES (4, '山海经·鲛人泪/人鱼公主', '人鱼失了心跳，甘心赴你的毒药。沧海月明珠有泪，苍洱夜冰玉生烟', 1, 0, NULL, 'https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_d7260c3f-87de-4a7a-b79a-19e6a373b235.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_8d61cb2d-a0e5-4d34-9fae-ff2a9abe4324.png,https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1686750816208478210_e52930b6-4bf2-4a15-92ad-0ba4a1eb1304.png', NULL, 'default',19000);


-- ----------------------------
-- Table structure for model_template
-- ----------------------------
DROP TABLE IF EXISTS `model_template_type`;
CREATE TABLE `model_template_type`  (
                                   `template_type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `template_type_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类别名称',
                                   `del_flag` bigint(4) NULL DEFAULT NULL COMMENT '删除标识',
                                   `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`template_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
