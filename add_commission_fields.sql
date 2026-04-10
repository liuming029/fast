-- 给订单表添加骑手分成和平台分成字段
ALTER TABLE `order` ADD COLUMN `rider_commission` DECIMAL(10,2) DEFAULT NULL COMMENT '骑手分成';
ALTER TABLE `order` ADD COLUMN `platform_commission` DECIMAL(10,2) DEFAULT NULL COMMENT '平台分成';
