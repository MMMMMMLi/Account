/*
Navicat MySQL Data Transfer

Source Server         : account-aliyun-mysql
Source Server Version : 50640
Source Host           : 39.105.5.251:23333
Source Database       : account

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2020-11-10 16:15:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(255) DEFAULT NULL COMMENT '订单用户ID',
  `createBy` varchar(255) DEFAULT NULL COMMENT '创建者ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `applyBox` int(4) DEFAULT NULL COMMENT '使用框子数量',
  `retreatBox` int(4) DEFAULT NULL COMMENT '退框子数量',
  `totalPrice` decimal(10,2) DEFAULT NULL COMMENT '总价钱',
  `totalWeight` double(10,2) DEFAULT NULL COMMENT '总重量',
  `status` tinyint(1) DEFAULT '0' COMMENT '订单状态；0：未付款，1：已付款',
  `collectionTime` datetime DEFAULT NULL COMMENT '收款时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COMMENT='订单信息';

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('53', '565793f0-9051-452a-a885-3b65897c3cd7', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-21 15:33:46', '2020-10-22 15:33:46', '12', '2', '63438.00', '3529.00', '0', null);
INSERT INTO `order_info` VALUES ('55', '3f43c986-ff19-488c-a76d-840b9a84f3b9', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-22 15:34:07', '2020-10-22 15:34:07', '12', '2', '1740.00', '120.00', '0', null);
INSERT INTO `order_info` VALUES ('57', '565793f0-9051-452a-a885-3b65897c3cd7', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-25 17:12:07', '2020-10-25 17:12:07', '12', '2', '15300.00', '2000.00', '0', null);
INSERT INTO `order_info` VALUES ('58', '3f43c986-ff19-488c-a76d-840b9a84f3b11', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-26 05:41:30', '2020-10-26 05:41:30', '2', '2', '25908.00', '12409.00', '0', null);
INSERT INTO `order_info` VALUES ('61', '565793f0-9051-452a-a885-3b65897c3cd7', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-26 10:42:00', '2020-10-26 10:42:21', '12', '2', '2152428.00', '1019.00', '0', null);
INSERT INTO `order_info` VALUES ('64', '565793f0-9051-452a-a885-3b65897c3cd7', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-27 03:40:48', '2020-10-27 03:40:48', '100', '10', '27700.00', '2000.00', '1', '2020-10-27 15:37:37');
INSERT INTO `order_info` VALUES ('66', '3f43c986-ff19-488c-a76d-840b9a84f3b10', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-27 06:42:52', '2020-10-27 06:42:52', '12', '2', '2700.00', '1200.00', '0', null);
INSERT INTO `order_info` VALUES ('67', '3f43c986-ff19-488c-a76d-840b9a84f3b11', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-27 12:42:59', '2020-10-27 12:42:59', '12', '12', '48400.00', '2200.00', '0', null);
INSERT INTO `order_info` VALUES ('68', '3f43c986-ff19-488c-a76d-840b9a84f3b9', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-27 13:43:06', '2020-10-27 13:43:06', '2', '2', '44000.00', '2000.00', '0', null);
INSERT INTO `order_info` VALUES ('69', '565793f0-9051-452a-a885-3b65897c3cd7', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-27 16:32:42', '2020-10-27 16:32:42', '12', '2', '36060.00', '2980.00', '0', null);
INSERT INTO `order_info` VALUES ('70', '565793f0-9051-452a-a885-3b65897c3cd7', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-28 18:15:11', '2020-10-28 18:15:11', '12', '2', '12300.00', '1000.00', '0', null);
INSERT INTO `order_info` VALUES ('72', '3f43c986-ff19-488c-a76d-840b9a84f3b11', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-28 18:15:52', '2020-10-28 18:15:52', '35', '36', '719970.00', '2400.00', '0', null);
INSERT INTO `order_info` VALUES ('73', '3f43c986-ff19-488c-a76d-840b9a84f3b9', '565793f0-9051-452a-a885-3b65897c3cd7', '2020-10-28 18:16:12', '2020-10-28 18:16:12', '20', '60', '334800.00', '2800.00', '1', '2020-10-28 18:21:24');
INSERT INTO `order_info` VALUES ('74', '6ac8471b-395e-4293-8a4d-678d653a85ba', '6ac8471b-395e-4293-8a4d-678d653a85ba', '2020-10-28 20:04:32', '2020-10-28 20:04:32', '20', '15', '14706.00', '12130.00', '0', null);

-- ----------------------------
-- Table structure for order_info_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_info_detail`;
CREATE TABLE `order_info_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderId` int(11) DEFAULT NULL COMMENT '订单ID',
  `categoryValue` varchar(200) DEFAULT NULL COMMENT '品种规格',
  `sizeValue` varchar(200) DEFAULT NULL COMMENT '大小规格',
  `gross` decimal(10,2) DEFAULT NULL COMMENT '毛重',
  `tare` decimal(10,2) DEFAULT NULL COMMENT '皮重',
  `unitPrice` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `totalPrice` decimal(10,2) DEFAULT NULL COMMENT '总价钱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COMMENT='订单详情';

-- ----------------------------
-- Records of order_info_detail
-- ----------------------------
INSERT INTO `order_info_detail` VALUES ('68', '53', '西瓜红', '特大', '1231.00', '12.00', '12.00', '14628.00');
INSERT INTO `order_info_detail` VALUES ('69', '53', '济薯26', '大', '2312.00', '2.00', '21.00', '48510.00');
INSERT INTO `order_info_detail` VALUES ('71', '55', '烟薯25', '中', '122.00', '2.00', '12.00', '1440.00');
INSERT INTO `order_info_detail` VALUES ('72', '56', '西瓜红', '特大', '121.00', '22.00', '2.00', '198.00');
INSERT INTO `order_info_detail` VALUES ('73', '56', '济薯26', '大', '12312.00', '212.00', '12.00', '145200.00');
INSERT INTO `order_info_detail` VALUES ('74', '57', '西瓜红', '特大', '1200.00', '200.00', '10.00', '10000.00');
INSERT INTO `order_info_detail` VALUES ('75', '57', '济薯26', '大', '1200.00', '200.00', '5.00', '5000.00');
INSERT INTO `order_info_detail` VALUES ('76', '58', '西瓜红', '特大', '12312.00', '12.00', '2.00', '24600.00');
INSERT INTO `order_info_detail` VALUES ('77', '58', '济薯26', '大', '121.00', '12.00', '12.00', '1308.00');
INSERT INTO `order_info_detail` VALUES ('81', '61', '西瓜红', '特大', '1231.00', '212.00', '2112.00', '2152128.00');
INSERT INTO `order_info_detail` VALUES ('82', '62', '烟薯25', '中', '1212.00', '122.00', '12.00', '13080.00');
INSERT INTO `order_info_detail` VALUES ('83', '62', '烟薯25', '小', '1000.00', '200.00', '6.00', '4800.00');
INSERT INTO `order_info_detail` VALUES ('84', '63', '测试A', '中', '1222.00', '222.00', '12.00', '12000.00');
INSERT INTO `order_info_detail` VALUES ('85', '64', '测试B', '中', '1200.00', '200.00', '10.00', '10000.00');
INSERT INTO `order_info_detail` VALUES ('86', '64', '西瓜红', '大', '1200.00', '200.00', '15.00', '15000.00');
INSERT INTO `order_info_detail` VALUES ('87', '65', '西瓜红', '特大', '1212.00', '212.00', '23.00', '23000.00');
INSERT INTO `order_info_detail` VALUES ('88', '66', '西瓜红', '特大', '1212.00', '12.00', '2.00', '2400.00');
INSERT INTO `order_info_detail` VALUES ('89', '67', '西瓜红', '特大', '2222.00', '22.00', '22.00', '48400.00');
INSERT INTO `order_info_detail` VALUES ('90', '68', '西瓜红', '特大', '2222.00', '222.00', '22.00', '44000.00');
INSERT INTO `order_info_detail` VALUES ('91', '69', '测试C', '特大', '1220.00', '220.00', '12.00', '12000.00');
INSERT INTO `order_info_detail` VALUES ('92', '69', '测试A', '小', '2000.00', '20.00', '12.00', '23760.00');
INSERT INTO `order_info_detail` VALUES ('93', '70', '西瓜红', '特大', '1200.00', '200.00', '12.00', '12000.00');
INSERT INTO `order_info_detail` VALUES ('94', '71', '测试C', '小', '1300.00', '300.00', '18.00', '18000.00');
INSERT INTO `order_info_detail` VALUES ('95', '72', '烟薯25', '中', '3000.00', '600.00', '300.00', '720000.00');
INSERT INTO `order_info_detail` VALUES ('96', '73', '测试A', '特大', '3000.00', '200.00', '120.00', '336000.00');
INSERT INTO `order_info_detail` VALUES ('97', '74', '测试C', '小', '12330.00', '200.00', '1.20', '14556.00');

-- ----------------------------
-- Table structure for sys_banners
-- ----------------------------
DROP TABLE IF EXISTS `sys_banners`;
CREATE TABLE `sys_banners` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `picUrlPre` varchar(255) NOT NULL COMMENT '照片前缀',
  `picName` varchar(255) NOT NULL COMMENT '照片名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `orderType` int(3) DEFAULT NULL COMMENT '排序规则',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='首页轮播图';

-- ----------------------------
-- Records of sys_banners
-- ----------------------------
INSERT INTO `sys_banners` VALUES ('1', 'https://imengli.com/img/wechat', 'qiaoba.jpg', '1', '2');
INSERT INTO `sys_banners` VALUES ('2', 'https://imengli.com/img/wechat', 'lufei.jpg', '1', '3');
INSERT INTO `sys_banners` VALUES ('3', 'https://imengli.com/img/wechat', 'xiong.jpg', '1', '1');

-- ----------------------------
-- Table structure for sys_constant
-- ----------------------------
DROP TABLE IF EXISTS `sys_constant`;
CREATE TABLE `sys_constant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `sortFlag` tinyint(2) NOT NULL DEFAULT '99',
  `createDate` datetime DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='系统常量表';

-- ----------------------------
-- Records of sys_constant
-- ----------------------------
INSERT INTO `sys_constant` VALUES ('1', 'category', '西瓜红', 'web', '6', '2020-10-12 08:43:22', '1');
INSERT INTO `sys_constant` VALUES ('2', 'category', '济薯26', 'web', '5', '2020-10-12 08:43:22', '1');
INSERT INTO `sys_constant` VALUES ('3', 'category', '烟薯25', 'web', '4', '2020-10-12 08:43:22', '1');
INSERT INTO `sys_constant` VALUES ('4', 'category', '测试A', 'web', '3', '2020-10-12 08:43:22', '1');
INSERT INTO `sys_constant` VALUES ('5', 'category', '测试B', 'web', '2', '2020-10-12 08:43:22', '1');
INSERT INTO `sys_constant` VALUES ('6', 'category', '测试C', 'web', '1', '2020-10-12 08:43:22', '1');
INSERT INTO `sys_constant` VALUES ('7', 'size', '特大', 'web', '4', '2020-10-12 08:44:25', '1');
INSERT INTO `sys_constant` VALUES ('8', 'size', '大', 'web', '3', '2020-10-12 08:44:25', '1');
INSERT INTO `sys_constant` VALUES ('9', 'size', '中', 'web', '2', '2020-10-12 08:44:25', '1');
INSERT INTO `sys_constant` VALUES ('10', 'size', '小', 'web', '1', '2020-10-12 08:44:25', '1');
INSERT INTO `sys_constant` VALUES ('12', '0', '待付款', 'paidStatus', '1', '2020-10-12 08:59:33', '1');
INSERT INTO `sys_constant` VALUES ('13', '1', '已付款', 'paidStatus', '2', '2020-10-12 08:59:37', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `useable` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(100) NOT NULL,
  `userName` varchar(255) DEFAULT NULL COMMENT '用户名',
  `userNameCode` varchar(255) DEFAULT NULL COMMENT '用户拼音',
  `phoneNumber` varchar(11) DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) DEFAULT NULL COMMENT '用户地址',
  `nickName` varchar(255) DEFAULT NULL COMMENT '微信昵称',
  `avatarUrl` varchar(255) DEFAULT NULL COMMENT '头像连接',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `lastLoginTimeStamp` bigint(14) DEFAULT NULL COMMENT '最后登陆时间',
  `state` int(1) DEFAULT NULL COMMENT '状态。0：失效，1：正常，2：待审核。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b10', '小红', 'xh', '15562575298', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', '2020-11-10 15:57:02', null, null, null);
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b11', '小白', 'xb', '13791921198', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', '2020-11-10 15:57:02', null, null, null);
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b9', '小明', 'xm', '18369655260', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', '2020-11-10 15:57:02', null, null, null);
INSERT INTO `sys_user` VALUES ('565793f0-9051-452a-a885-3b65897c3cd7', '姜伟加', 'jwj', '18369655298', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sibBsXW9vUtCAYaIW1nHu8icA/132', '1', 'China', 'Shandong', 'Qingdao', '2020-11-10 15:57:02', '2020-11-10 15:57:21', null, null);
INSERT INTO `sys_user` VALUES ('6ac8471b-395e-4293-8a4d-678d653a85ba', '刘奉英', 'lfy', '13791012330', '鲁能泰山七号一期', 'MengLi', 'https://thirdwx.qlogo.cn/mmopen/vi_32/4FQn2qGnNBmTRDQsYgV9Av5ZgHb03W7RwwjYjibWFkfLxAC9z1WuBlyfgskRHbcQibhic47X3scXxYJv9QQdmHD8g/132', '2', 'China', 'Shandong', 'Jinan', '2020-11-10 15:57:02', null, null, null);

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user` (
  `openId` varchar(40) NOT NULL COMMENT '小程序系统唯一ID',
  `unionId` varchar(255) DEFAULT NULL COMMENT '唯一标识',
  `userId` varchar(255) DEFAULT NULL COMMENT '用户表ID',
  PRIMARY KEY (`openId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户信息';

-- ----------------------------
-- Records of wechat_user
-- ----------------------------
INSERT INTO `wechat_user` VALUES ('omDO35BQ6TRzwAlP_eFkEWP7npa8', null, null);
INSERT INTO `wechat_user` VALUES ('omDO35PBEyq4Y_XWzX_6QcfrhMlE', null, '6ac8471b-395e-4293-8a4d-678d653a85ba');
INSERT INTO `wechat_user` VALUES ('omDO35PJepvnO4vMMW9h3iB96fBQ', null, '565793f0-9051-452a-a885-3b65897c3cd7');
