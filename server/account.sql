/*
Navicat MySQL Data Transfer

Source Server         : account-aliyun-mysql
Source Server Version : 50640
Source Host           : 39.105.5.251:23333
Source Database       : account

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2020-10-12 11:11:52
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
  `applyBox` int(4) DEFAULT NULL COMMENT '使用框子数量',
  `retreatBox` int(4) DEFAULT NULL COMMENT '退框子数量',
  `totalPrice` decimal(10,2) DEFAULT NULL COMMENT '总价钱',
  `totalWeight` double(10,2) DEFAULT NULL COMMENT '总重量',
  `status` tinyint(1) DEFAULT '0' COMMENT '订单状态；0：未付款，1：已付款',
  `collectionTime` datetime DEFAULT NULL COMMENT '收款时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COMMENT='订单信息';

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('38', '3f43c986-ff19-488c-a76d-840b9a84f3b8', 'ed9483e6-e76a-485b-a271-552feb8d384a', '2020-10-03 09:57:39', '3', '2', '530.00', '100.00', '0', null);
INSERT INTO `order_info` VALUES ('39', '3f43c986-ff19-488c-a76d-840b9a84f3b9', 'ed9483e6-e76a-485b-a271-552feb8d384a', '2020-10-09 09:23:46', '12', '12', '2400.00', '1200.00', '0', null);
INSERT INTO `order_info` VALUES ('40', 'ed9483e6-e76a-485b-a271-552feb8d384a', 'ed9483e6-e76a-485b-a271-552feb8d384a', '2020-10-10 14:46:03', '12', '2', '320254.00', '25572.00', '0', null);
INSERT INTO `order_info` VALUES ('41', '3f43c986-ff19-488c-a76d-840b9a84f3b11', 'ed9483e6-e76a-485b-a271-552feb8d384a', '2020-10-12 11:01:59', '12', '1', '133302.00', '11081.00', '0', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COMMENT='订单详情';

-- ----------------------------
-- Records of order_info_detail
-- ----------------------------
INSERT INTO `order_info_detail` VALUES ('31', '30', '西瓜红', '大', '1211.00', '211.00', '2.00', '2000.00');
INSERT INTO `order_info_detail` VALUES ('32', '30', '济薯26', '中', '122.00', '22.00', '12.00', '1200.00');
INSERT INTO `order_info_detail` VALUES ('33', '31', '西瓜红', '大', '1211.00', '12.00', '1.50', '1798.50');
INSERT INTO `order_info_detail` VALUES ('34', '32', '西瓜红', '大', '1211.00', '232.00', '1.50', '1468.50');
INSERT INTO `order_info_detail` VALUES ('35', '33', '西瓜红', '大', '1212.00', '12.00', '12.00', '14400.00');
INSERT INTO `order_info_detail` VALUES ('36', '34', '烟薯25', '中', '122.00', '22.00', '12.00', '1200.00');
INSERT INTO `order_info_detail` VALUES ('37', '34', '西瓜红', '中', '122.00', '22.00', '2.00', '200.00');
INSERT INTO `order_info_detail` VALUES ('38', '34', '济薯26', '中', '122.00', '21.00', '12.00', '1212.00');
INSERT INTO `order_info_detail` VALUES ('39', '34', '济薯26', '小', '1222.00', '121.00', '12.00', '13212.00');
INSERT INTO `order_info_detail` VALUES ('40', '35', '西瓜红', '大', '1212.00', '12.00', '12.00', '14400.00');
INSERT INTO `order_info_detail` VALUES ('41', '36', '济薯26', '大', '120.00', '100.00', '2.50', '50.00');
INSERT INTO `order_info_detail` VALUES ('42', '36', '西瓜红', '中', '150.00', '100.00', '5.00', '250.00');
INSERT INTO `order_info_detail` VALUES ('43', '37', '西瓜红', '大', '250.00', '200.00', '5.00', '250.00');
INSERT INTO `order_info_detail` VALUES ('44', '38', '西瓜红', '大', '200.00', '100.00', '5.00', '500.00');
INSERT INTO `order_info_detail` VALUES ('45', '39', '西瓜红', '大', '1212.00', '12.00', '2.00', '2400.00');
INSERT INTO `order_info_detail` VALUES ('46', '40', '烟薯25', '大', '12312.00', '121.00', '12.00', '146292.00');
INSERT INTO `order_info_detail` VALUES ('47', '40', '济薯26', '中', '1212.00', '22.00', '23.00', '27370.00');
INSERT INTO `order_info_detail` VALUES ('48', '40', '西瓜红', '小', '12312.00', '121.00', '12.00', '146292.00');
INSERT INTO `order_info_detail` VALUES ('49', '41', '测试C', '中', '12312.00', '1231.00', '12.00', '132972.00');

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
  `lastLoginTimeStamp` bigint(14) DEFAULT NULL COMMENT '最后登陆时间',
  `state` int(1) DEFAULT NULL COMMENT '状态。0：失效，1：正常，2：待审核。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b10', '小红', 'xh', '15562575298', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', null, null);
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b11', '小白', 'xb', '13791921198', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', null, null);
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b8', '刘奉英', 'lfy', '13791012330', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', null, null);
INSERT INTO `sys_user` VALUES ('3f43c986-ff19-488c-a76d-840b9a84f3b9', '小明', 'xm', '18369655260', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', null, null);
INSERT INTO `sys_user` VALUES ('ed9483e6-e76a-485b-a271-552feb8d384a', '姜伟加', 'jwj', '18369655298', '鲁能泰山七号', 'MEngLI', 'https://thirdwx.qlogo.cn/mmopen/vi_32/8ArFiaWEibhgMSPp10St5gzA5Riaicgz3heqV2V6pmAx6UogRQY5Dw8qljq2530Xpy5sTlYSx6iaTMSF5dgFQrvdNng/132', '1', 'China', 'Shandong', 'Qingdao', null, null);

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
INSERT INTO `wechat_user` VALUES ('omDO35PJepvnO4vMMW9h3iB96fBQ', null, 'ed9483e6-e76a-485b-a271-552feb8d384a');
