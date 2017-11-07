
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `STORE`
-- ----------------------------
DROP TABLE IF EXISTS `STORE`;
CREATE TABLE `STORE` (
 		`STORE_ID` varchar(100) NOT NULL,
		`ID` int(11) NOT NULL COMMENT '标识',
		`STORENAME` varchar(50) DEFAULT NULL COMMENT '名称',
		`STOREDISTRICT` varchar(200) DEFAULT NULL COMMENT '区域',
		`STOREADDRESS` varchar(500) DEFAULT NULL COMMENT '地址',
		`CREATEDATE` varchar(19) DEFAULT NULL COMMENT '创建时间',
		`LASTDATE` varchar(19) DEFAULT NULL COMMENT '最后编辑时间',
		`STORESTATUSID` int(11) NOT NULL COMMENT '状态',
  		PRIMARY KEY (`STORE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
