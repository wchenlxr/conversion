-- Create table
create table COMPANY
(
  company_code NUMBER not null,
  company_name VARCHAR2(100),
  create_time  DATE,
  update_time  DATE,
  status       INTEGER,
  remark       VARCHAR2(500)
)
tablespace HBF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table COMPANY
  is '厂商信息';
-- Add comments to the columns 
comment on column COMPANY.company_code
  is '厂商编码';
comment on column COMPANY.company_name
  is '厂商名称';
comment on column COMPANY.create_time
  is '创建日期';
comment on column COMPANY.update_time
  is '更新日期';
comment on column COMPANY.status
  is '有效标志1有效,0无效';
comment on column COMPANY.remark
  is '描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table COMPANY
  add constraint PK_COMPANY primary key (COMPANY_CODE)
  using index 
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

  
  -- Create table
create table CONTRAST
(
  id                   NUMBER not null,
  company_code         VARCHAR2(100),
  platform_range_code  VARCHAR2(100),
  company_range_code   VARCHAR2(100),
  platform_detail_name VARCHAR2(200),
  platform_detail_code VARCHAR2(100),
  score                NUMBER(12,6),
  selected             INTEGER
)
tablespace HBF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table CONTRAST
  is '搜索对照排行临时表';
-- Add comments to the columns 
comment on column CONTRAST.id
  is '主键';
comment on column CONTRAST.company_code
  is '厂商编码';
comment on column CONTRAST.platform_range_code
  is '平台分类编码';
comment on column CONTRAST.company_range_code
  is '厂商值编码';
comment on column CONTRAST.platform_detail_name
  is '平台值名称';
comment on column CONTRAST.platform_detail_code
  is '平台值编码';
comment on column CONTRAST.score
  is '匹配得分';
comment on column CONTRAST.selected
  is '选中';
-- Create/Recreate indexes 
create index INDEX_1 on CONTRAST (COMPANY_CODE, PLATFORM_RANGE_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
create index INDEX_COMPANY_CODE on CONTRAST (COMPANY_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index INDEX_PLATFORM_RANGE_CODE on CONTRAST (PLATFORM_RANGE_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table CONTRAST
  add constraint PK_ID primary key (ID)
  using index 
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

  
  
  -- Create table
create table PLATFORM_RANGE_CLASS
(
  id            NUMBER not null,
  platform_code VARCHAR2(100),
  platform_name VARCHAR2(200),
  status        INTEGER,
  create_time   DATE,
  update_time   DATE,
  remark        VARCHAR2(500)
)
tablespace HBF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PLATFORM_RANGE_CLASS
  is '亚德人口健康平台标准值域代码分类表';
-- Add comments to the columns 
comment on column PLATFORM_RANGE_CLASS.id
  is '主健';
comment on column PLATFORM_RANGE_CLASS.platform_code
  is '分类编码';
comment on column PLATFORM_RANGE_CLASS.platform_name
  is '分类名称';
comment on column PLATFORM_RANGE_CLASS.status
  is '有效标志,1有效,0无效';
comment on column PLATFORM_RANGE_CLASS.create_time
  is '创建日期';
comment on column PLATFORM_RANGE_CLASS.update_time
  is '更新日期';
comment on column PLATFORM_RANGE_CLASS.remark
  is '描述';
-- Create/Recreate primary, unique and foreign key constraints 
alter table PLATFORM_RANGE_CLASS
  add constraint PK_PLATFORM_RANGE_CLASS primary key (ID)
  using index 
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table PLATFORM_RANGE_CLASS
  add constraint PK_PLATFORM_CODE unique (PLATFORM_CODE)
  using index 
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

  
  -- Create table
create table PLATFORM_RANGE_CONTRAST
(
  id                   NUMBER not null,
  company_code         VARCHAR2(100),
  company_name         VARCHAR2(200),
  platform_range_code  VARCHAR2(100),
  platform_range_name  VARCHAR2(200),
  company_range_code   VARCHAR2(100),
  company_range_name   VARCHAR2(100),
  platform_detail_code VARCHAR2(100),
  platform_detail_name VARCHAR2(200),
  contrast_time        DATE
)
tablespace HBF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PLATFORM_RANGE_CONTRAST
  is '厂商值域对照表';
-- Add comments to the columns 
comment on column PLATFORM_RANGE_CONTRAST.id
  is '主健';
comment on column PLATFORM_RANGE_CONTRAST.company_code
  is '厂商编码';
comment on column PLATFORM_RANGE_CONTRAST.company_name
  is '厂商名称';
comment on column PLATFORM_RANGE_CONTRAST.platform_range_code
  is '平台值域大类编码';
comment on column PLATFORM_RANGE_CONTRAST.platform_range_name
  is '平台值域大类名称';
comment on column PLATFORM_RANGE_CONTRAST.company_range_code
  is '厂商值域编码';
comment on column PLATFORM_RANGE_CONTRAST.company_range_name
  is '厂商值域名称';
comment on column PLATFORM_RANGE_CONTRAST.platform_detail_code
  is '平台值编码';
comment on column PLATFORM_RANGE_CONTRAST.platform_detail_name
  is '平台值名称';
comment on column PLATFORM_RANGE_CONTRAST.contrast_time
  is '对比日期';
-- Create/Recreate indexes 
create index INDEX_COMPANY on PLATFORM_RANGE_CONTRAST (COMPANY_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index INDEX_PLATFORM_DETAIL_CODE on PLATFORM_RANGE_CONTRAST (COMPANY_CODE, PLATFORM_DETAIL_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index INDEX_PLATFORM_RANGE_CODE1 on PLATFORM_RANGE_CONTRAST (COMPANY_CODE, PLATFORM_RANGE_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table PLATFORM_RANGE_CONTRAST
  add constraint PK_PLATFORM_RANGE_CONTRAST primary key (ID)
  using index 
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

  
  -- Create table
create table PLATFORM_RANGE_DETAIL
(
  id            NUMBER not null,
  platform_code VARCHAR2(100) not null,
  detail_code   VARCHAR2(100),
  detail_name   VARCHAR2(200),
  index_status  INTEGER default 0
)
tablespace HBF
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table PLATFORM_RANGE_DETAIL
  is '平台值域分类明细表';
-- Add comments to the columns 
comment on column PLATFORM_RANGE_DETAIL.id
  is '主健';
comment on column PLATFORM_RANGE_DETAIL.platform_code
  is '平台值域分类编码';
comment on column PLATFORM_RANGE_DETAIL.detail_code
  is '值编码';
comment on column PLATFORM_RANGE_DETAIL.detail_name
  is '值名称';
comment on column PLATFORM_RANGE_DETAIL.index_status
  is '索引状态0未索引,1已索引';
-- Create/Recreate indexes
create index INDEX_INDEX_STATUS on PLATFORM_RANGE_DETAIL (INDEX_STATUS)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index INDEX_PLATFORM_CODE on PLATFORM_RANGE_DETAIL (PLATFORM_CODE)
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table PLATFORM_RANGE_DETAIL
  add constraint PK_PLATFORM_RANGE_DETAIL primary key (ID)
  using index 
  tablespace HBF
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
