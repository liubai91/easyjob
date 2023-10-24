
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for easyjob_job
-- ----------------------------
DROP TABLE IF EXISTS `easyjob_job`;
CREATE TABLE `easyjob_job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `description` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `trigger_type` varchar(255) DEFAULT NULL COMMENT '触发类型',
  `target_object` varchar(255) DEFAULT NULL COMMENT '触发对象',
  `target_method` varchar(255) DEFAULT NULL COMMENT '触发方法',
  `target_params` text COMMENT '任务参数',
  `target_class` varchar(255) DEFAULT NULL COMMENT '触发类',
  `cron_expression` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'cron表达式',
  `fire_time` datetime DEFAULT NULL COMMENT '单次任务触发时间',
  `luna_enable` bit(1) DEFAULT b'0' COMMENT '是否启用',
  `strategy` varchar(255) DEFAULT NULL COMMENT '异常后策略',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=utf8mb4 COMMENT='系统管理-任务计划';



-- ----------------------------
-- Table structure for easyjob_joblog
-- ----------------------------
DROP TABLE IF EXISTS `easyjob_joblog`;
CREATE TABLE `easyjob_joblog` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '版本名称',
  `start_time` bigint DEFAULT NULL COMMENT '任务开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '任务结束时间',
  `duration` bigint DEFAULT NULL COMMENT '执行时间',
  `state` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `luna_log` varchar(255) DEFAULT NULL COMMENT '日志',
  `exception_brief` text COMMENT '异常概述',
  `exception_detail` text COMMENT '异常详情',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=121999 DEFAULT CHARSET=utf8mb4 COMMENT='系统管理-任务记录';



-- ----------------------------
-- Table structure for easyjob_scheduler
-- ----------------------------
DROP TABLE IF EXISTS `easyjob_scheduler`;
CREATE TABLE `easyjob_scheduler` (
  `status` tinyint(1) DEFAULT '0' COMMENT '调度器初始状态，1-开启，0-不开启'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of easyjob_scheduler
-- ----------------------------
BEGIN;
INSERT INTO `easyjob_scheduler` (`status`) VALUES (0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
