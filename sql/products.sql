-- charmai.products definition

CREATE TABLE `products` (
                            `product_id` varchar(32) NOT NULL,
                            `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
                            `point` bigint(20) DEFAULT NULL COMMENT '积分',
                            `price` decimal(10,2) NOT NULL COMMENT '价格',
                            `description` text COMMENT '描述',
                            `del_flag` char(2) DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
                            PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;