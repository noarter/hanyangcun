-- MySQL dump 10.13  Distrib 5.7.22, for osx10.13 (x86_64)
--
-- Host: 127.0.0.1    Database: village
-- ------------------------------------------------------
-- Server version	5.7.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_account`
--

DROP TABLE IF EXISTS `t_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL COMMENT '姓名',
  `phone` int(11) NOT NULL COMMENT '手机号',
  `sex` int(1) NOT NULL COMMENT '性别：0-男1-女',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_account_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account`
--

LOCK TABLES `t_account` WRITE;
/*!40000 ALTER TABLE `t_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_activity`
--

DROP TABLE IF EXISTS `t_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL COMMENT '活动名称',
  `content` text NOT NULL COMMENT '活动内容',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `discount_price` float NOT NULL COMMENT '优惠券金额',
  `use_count` int(11) NOT NULL COMMENT '使用数量',
  `status` int(1) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_activity_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_activity`
--

LOCK TABLES `t_activity` WRITE;
/*!40000 ALTER TABLE `t_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order`
--

DROP TABLE IF EXISTS `t_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL COMMENT '订单号',
  `order_no` varchar(64) NOT NULL COMMENT '订单编号',
  `order_type` int(1) NOT NULL COMMENT '订单类型',
  `from_the_time` varchar(128) NOT NULL COMMENT '入离时间',
  `rooms_number` int(11) NOT NULL COMMENT '房间套数',
  `nights` int(11) NOT NULL COMMENT '入住晚数',
  `people` int(11) NOT NULL COMMENT '人数',
  `link_name` varchar(64) NOT NULL COMMENT '联系人',
  `link_phone` int(11) NOT NULL COMMENT '联系人手机号',
  `guests` varchar(64) NOT NULL COMMENT '客人',
  `guests_phone` int(11) NOT NULL COMMENT '客人手机号',
  `order_total` float NOT NULL DEFAULT '0' COMMENT '订单金额',
  `discount_price` float NOT NULL DEFAULT '0' COMMENT '优惠价格',
  `actual_amount` float NOT NULL DEFAULT '0' COMMENT '实付金额',
  `order_status` int(1) NOT NULL COMMENT '订单状态',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_order_id_uindex` (`id`),
  UNIQUE KEY `t_order_order_no_uindex` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order`
--

LOCK TABLES `t_order` WRITE;
/*!40000 ALTER TABLE `t_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_rooms`
--

DROP TABLE IF EXISTS `t_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_rooms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL COMMENT '房间类型',
  `room_number` int(11) NOT NULL DEFAULT '0' COMMENT '房间总数',
  `used_room` int(11) NOT NULL DEFAULT '0' COMMENT '已定房间数量',
  `remained_room` int(11) NOT NULL COMMENT '剩余房间数量',
  `room_price` float NOT NULL COMMENT '房间单价',
  `discount_price` float NOT NULL DEFAULT '0' COMMENT '折扣价',
  `discount_rate` int(11) NOT NULL DEFAULT '0' COMMENT '折扣比率',
  `week_date` varchar(64) DEFAULT NULL COMMENT '周期',
  `status` int(1) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_rooms_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_rooms`
--

LOCK TABLES `t_rooms` WRITE;
/*!40000 ALTER TABLE `t_rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `salt` varchar(128) NOT NULL COMMENT '盐值',
  `locked` int(1) NOT NULL DEFAULT '0' COMMENT '0-正常1-禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_id_uindex` (`id`),
  UNIQUE KEY `t_user_username_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','fbab6955560ff043f0f011523248e352b9bd51a27c7f9e754619905dd8d48abe7ba67dc49931867fb09cd677ade5bb3ee6a3fbe753cd89a14b4ab90e705b4ab4','ADDA1D39DAAD44F497551D64D494F8EB',0,'2018-05-07 16:05:20','2018-05-07 16:05:23');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-14 22:41:53
