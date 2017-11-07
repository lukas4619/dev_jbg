-- ----------------------------
-- Table structure for "C##FHADMIN"."STORE"
-- ----------------------------
-- DROP TABLE "C##FHADMIN"."STORE";
CREATE TABLE "C##FHADMIN"."STORE" (
	"ID" NUMBER(11) NULL ,
	"STORENAME" VARCHAR2(50 BYTE) NULL ,
	"STOREDISTRICT" VARCHAR2(200 BYTE) NULL ,
	"STOREADDRESS" VARCHAR2(500 BYTE) NULL ,
	"CREATEDATE" VARCHAR2(19 BYTE) NULL ,
	"LASTDATE" VARCHAR2(19 BYTE) NULL ,
	"STORESTATUSID" NUMBER(11) NULL ,
	"STORE_ID" VARCHAR2(100 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE
;

COMMENT ON COLUMN "C##FHADMIN"."STORE"."ID" IS '标识';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."STORENAME" IS '名称';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."STOREDISTRICT" IS '区域';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."STOREADDRESS" IS '地址';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."CREATEDATE" IS '创建时间';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."LASTDATE" IS '最后编辑时间';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."STORESTATUSID" IS '状态';
COMMENT ON COLUMN "C##FHADMIN"."STORE"."STORE_ID" IS 'ID';

-- ----------------------------
-- Indexes structure for table STORE
-- ----------------------------

-- ----------------------------
-- Checks structure for table "C##FHADMIN"."STORE"

-- ----------------------------

ALTER TABLE "C##FHADMIN"."STORE" ADD CHECK ("STORE_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table "C##FHADMIN"."STORE"
-- ----------------------------
ALTER TABLE "C##FHADMIN"."STORE" ADD PRIMARY KEY ("STORE_ID");
