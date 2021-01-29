/*
Navicat MySQL Data Transfer

Source Server         : account-aliyun-mysql
Source Server Version : 50640
Source Host           : 39.105.5.251:23333
Source Database       : account

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2021-01-29 14:13:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(24) NOT NULL,
  `userId` varchar(100) DEFAULT NULL COMMENT '订单用户ID',
  `createBy` varchar(100) DEFAULT NULL COMMENT '创建者ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `applyBox` int(4) DEFAULT NULL COMMENT '使用框子数量',
  `retreatBox` int(4) DEFAULT NULL COMMENT '退框子数量',
  `totalPrice` decimal(10,2) DEFAULT NULL COMMENT '总价钱',
  `totalWeight` double(10,2) DEFAULT NULL COMMENT '总重量',
  `status` tinyint(1) DEFAULT '0' COMMENT '订单状态；0：未付款，1：已付款',
  `collectionTime` datetime DEFAULT NULL COMMENT '收款时间',
  `isNotice` tinyint(1) DEFAULT '0' COMMENT '是否通知',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息';

-- ----------------------------
-- Table structure for order_info_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_info_detail`;
CREATE TABLE `order_info_detail` (
  `id` bigint(24) NOT NULL,
  `orderId` bigint(24) DEFAULT NULL COMMENT '订单ID',
  `categoryValue` varchar(200) DEFAULT NULL COMMENT '品种规格',
  `sizeValue` varchar(200) DEFAULT NULL COMMENT '大小规格',
  `gross` decimal(10,2) DEFAULT NULL COMMENT '毛重',
  `tare` decimal(10,2) DEFAULT NULL COMMENT '皮重',
  `unitPrice` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `totalPrice` decimal(10,2) DEFAULT NULL COMMENT '总价钱',
  PRIMARY KEY (`id`),
  KEY `idx_orderId` (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情';

-- ----------------------------
-- Table structure for stock_info
-- ----------------------------
DROP TABLE IF EXISTS `stock_info`;
CREATE TABLE `stock_info` (
  `key` tinyint(1) NOT NULL DEFAULT '0' COMMENT '库存操作类型 0：货物 1：框子',
  `category` varchar(25) NOT NULL COMMENT '品种',
  `size` varchar(255) DEFAULT NULL COMMENT '大小',
  `number` double NOT NULL COMMENT '重量',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `updateBy` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`key`,`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存信息表';

-- ----------------------------
-- Table structure for stock_info_detail
-- ----------------------------
DROP TABLE IF EXISTS `stock_info_detail`;
CREATE TABLE `stock_info_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` varchar(255) DEFAULT NULL COMMENT '操作库存用户ID',
  `orderId` varchar(255) DEFAULT NULL COMMENT '对应的订单id',
  `key` tinyint(1) NOT NULL DEFAULT '0' COMMENT '库存操作类型 0：货物 1：框子',
  `category` varchar(255) DEFAULT NULL COMMENT '品种',
  `size` varchar(255) DEFAULT NULL COMMENT '大小',
  `number` double NOT NULL COMMENT '重量',
  `type` varchar(10) NOT NULL DEFAULT '0' COMMENT '操作类型，add是加/ reduce是减/ align是校准库存',
  `operationDate` datetime DEFAULT NULL COMMENT '操作时间',
  `operationBy` varchar(255) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COMMENT='库存操作详情表';

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
-- Table structure for sys_constant
-- ----------------------------
DROP TABLE IF EXISTS `sys_constant`;
CREATE TABLE `sys_constant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='系统常量表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `useable` tinyint(1) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `entryPage` varchar(255) DEFAULT '/pages/order/order' COMMENT '每个角色默认的入口页面',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='简单的权限表';

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
  `lastLoginTime` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `state` int(1) DEFAULT '1' COMMENT '状态。0：冻结，1：正常',
  `subMsgNum` int(10) DEFAULT '0' COMMENT '用户接收消息提示的次数',
  `roleId` int(11) DEFAULT '1' COMMENT '权限ID',
  `isTemp` tinyint(1) DEFAULT '0' COMMENT '是否是测试用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

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
