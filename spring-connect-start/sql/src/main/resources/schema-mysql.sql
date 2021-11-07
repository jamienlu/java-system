CREATE TABLE IF NOT EXISTS `asura_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目id',
  `name` varchar(32)  NOT NULL COMMENT '类目名称',
  `parent_id` bigint(20) NOT NULL COMMENT '父类目id,顶级类目填0',
  `is_parent` tinyint(1) NOT NULL COMMENT '是否为父节点，true为是',
  `sort` int(4) NOT NULL COMMENT '排序指数，越小越靠前',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '分类表';

CREATE TABLE IF NOT EXISTS `asura_order`  (
  `id` varchar(32)  NOT NULL COMMENT 'order_id',
  `payment` bigint(30) NOT NULL COMMENT '支付金额 分',
  `payment_type` tinyint(1) NOT NULL COMMENT '支付类型 1-在线 0-到付',
  `status` int(3) NULL DEFAULT 1 COMMENT '1-未付款 2-已付款 3-未发货 4-已发货 5-成功 6-关闭',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `close_time` datetime(0) NULL DEFAULT NULL COMMENT '关闭时间',
  `shipping_id` bigint(20) NULL DEFAULT NULL COMMENT '物流id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_message` varchar(512)  NULL DEFAULT NULL COMMENT '用户留言',
  `user_nick_id` bigint(20) NULL DEFAULT NULL COMMENT '评价表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '订单表';

CREATE TABLE IF NOT EXISTS `asura_order_info`  (
  `id` varchar(32)  NOT NULL COMMENT 'asura_order_info_id',
  `sku_id` bigint(20) NOT NULL COMMENT '商品sku_id',
  `order_id` varchar(32)  NOT NULL COMMENT '订单',
  `count` int(10) NULL DEFAULT 1 COMMENT '数量',
  `title` varchar(256)  NOT NULL COMMENT '商品标题',
  `pic` varchar(256)  NOT NULL COMMENT '商品图片',
  `price` bigint(30) NULL DEFAULT NULL COMMENT '商品单价',
  `total_price` bigint(30) NULL DEFAULT NULL COMMENT '商品总价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '订单详情表';

CREATE TABLE IF NOT EXISTS `asura_order_shipping`  (
  `id` varchar(32)  NOT NULL COMMENT 'asura_order_shipping_id',
  `order_id` varchar(32)  NOT NULL COMMENT 'order_id',
  `ship_orz` varchar(32)  NOT NULL COMMENT '物流公司',
  `ship_real_id` varchar(32)  NOT NULL COMMENT '物流编号',
  `phone` varchar(20)  NOT NULL COMMENT '联系电话',
  `name` varchar(20)  NOT NULL COMMENT '联系人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `address` varchar(256)  NOT NULL COMMENT '收货地址',
  `statue` int(3) NULL DEFAULT NULL COMMENT '1-运送中 1-已到达 2-配送中 3-已签收 4-异常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '订单物流表';

CREATE TABLE IF NOT EXISTS `asura_shop_car`  (
  `id` varchar(32)  NOT NULL COMMENT 'order_id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `item_id` bigint(20) NOT NULL COMMENT 'sku id',
  `num` int(10) NOT NULL COMMENT '数量',
  `price` bigint(30) NOT NULL COMMENT '单价',
  `item_pic` varchar(256)  NULL DEFAULT NULL COMMENT '商品图片',
  `total_price` bigint(30) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '购物车' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `asura_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'sku id',
  `spu_id` bigint(20) NOT NULL COMMENT 'spu id',
  `title` varchar(256)  NOT NULL COMMENT '商品标题',
  `images` varchar(1024)  NULL DEFAULT '' COMMENT '商品的图片，多个图片以‘,’分割',
  `price` bigint(15) NOT NULL DEFAULT 0 COMMENT '销售价格，单位为分',
  `indexes` varchar(32)  NULL DEFAULT '' COMMENT '特有规格属性在spu属性模板中的对应下标组合',
  `own_spec` varchar(1024)  NULL DEFAULT '' COMMENT 'sku的特有规格参数键值对，json格式比如黑色31寸、白色32寸等',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效，true有效',
  `create_time` datetime(0) NOT NULL COMMENT '添加时间',
  `last_update_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = 'sku表,该表表示具体的商品实体' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `asura_spec_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cid` bigint(20) NOT NULL COMMENT '商品分类id，一个分类下有多个规格组',
  `name` varchar(32)  NOT NULL COMMENT '规格组名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `asura_user_info`  (
  `id` bigint(20) NOT NULL COMMENT 'asura_user_info_id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_desc` varchar(20)  NOT NULL COMMENT '用户描述',
  `score` bigint(20) NULL DEFAULT NULL COMMENT '积分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '用户明细表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `asura_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'asura_user_id',
  `name` varchar(20)  NOT NULL COMMENT '用户昵称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `phone` char(11)  NOT NULL COMMENT '手机号',
  `card_id` char(18)  NULL DEFAULT NULL COMMENT '身份id',
  `real_name` varchar(20)  NULL DEFAULT NULL COMMENT '真实名称',
  `statue` int(3) NULL DEFAULT NULL COMMENT '状态 0-激活',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '用户表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `asura_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'spu id',
  `title` varchar(128)  NOT NULL DEFAULT '' COMMENT '标题',
  `sub_title` varchar(256)  NULL DEFAULT '' COMMENT '子标题',
  `cid1` bigint(20) NOT NULL COMMENT '1级类目id',
  `cid2` bigint(20) NOT NULL COMMENT '2级类目id',
  `cid3` bigint(20) NOT NULL COMMENT '3级类目id',
  `brand_id` bigint(20) NOT NULL COMMENT '商品所属品牌id',
  `saleable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否上架，true上架',
  `valid` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效,true有效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = 'spu表，描述的是一个抽象性的商品' ROW_FORMAT = Dynamic;
