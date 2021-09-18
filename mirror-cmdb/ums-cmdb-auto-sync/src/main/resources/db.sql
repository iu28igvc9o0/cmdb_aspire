DROP TABLE IF EXISTS `cmdb_sync_form`;
CREATE TABLE `cmdb_sync_form` (
  `id` varchar(40) NOT NULL,
  `module_code` varchar(40) NOT NULL COMMENT '模型code',
  `form_code` varchar(40) NOT NULL DEFAULT '' COMMENT '表单code',
  `sync_code` varchar(40) NOT NULL COMMENT '远程code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='cmdb同步字段对应表';


insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('a5169484bc664ed999f6c5238167aa7a','m_pm','syncId','id');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('9222518cae0a44a1ac9e838d82fe5b46','m_pm','bizIp','bizIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('44ced5e6e2db45f485ea837bbe15a00c','m_pm','ipmiIp','ipmiIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('f38325c86bff4900855fb64a173bb6a7','m_pm','manageIp','manageIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('900191ccfe7745eab7160684bd385da9','m_pm','hostName','hostName');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('42c2baf0104a41f5989918ad53a1a38b','m_pm','roomId','roomId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('4cada68a2a7248ad8372daed18dce860','m_pm','bizSystem','bizSystem');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('18b6c8a9a67b413cb47a2d284378e29c','m_pm','statusId','statusId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('a677cfa0a41144d5b305a045262f371c','m_pm','categoryId','categoryId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('1c397daab4ed4bd1b22af8893f477918','m_pm','systemVersion','systemVersion');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('6ff8254b8c1447b492c6a869ffb98ff2','m_pm','hostEnv','hostEnv');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('423df0796d854cb8bdf1634f73a6a234','m_pm','snNo','snNo');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('5435540c60104ac88c6bbe50c4937952','m_pm','finNo','finNo');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('e1360f252b8c453daaac794e410ab120','m_pm','manufactor','manufactor');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('b3abd208d08f4147bc9004f5633782b3','m_pm','brand','brand');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('c14964a786ae42639ac2347e8ecd0258','m_pm','equipmentType','equipmentType');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('b49f1dce73484f86b21ace9f02903846','m_pm','mainStart','mainStartStr');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('aba02ec1359f4c2a805c1790794fbeb6','m_pm','mainEnd','mainEndStr');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('dc156338d6d04d7f831ecfb293d4ca85','m_pm','remark','remark');

insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('375d96da0153453a8d4fb740d9bd5f2a','m_vm','syncId','id');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('086768539f5448f19a2b13939ad1f362','m_vm','bizIp','bizIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('ca3244aa7e83426cb712bb8a4533cb7d','m_vm','ipmiIp','ipmiIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('89385111ffba45d08daeb63d5c5341d0','m_vm','manageIp','manageIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('21814ba4a7b24346b8e50c755f0d12cd','m_vm','parasiticIp','parasiticIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('319f1965ba614182a16226f1a3edf583','m_vm','hostName','hostName');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('33a54668bec1407cb35a9f6c279db25f','m_vm','roomId','roomId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('d3e7a0795358404c90552fe6e68e5019','m_vm','bizSystem','bizSystem');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('a1303f32196845a2addeb7161e5e63f8','m_vm','statusId','statusId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('2786bfc82d124d0ea1e1835ce3f52de0','m_vm','categoryId','categoryId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('38415c7cebdd4232bd41f3bd800e8334','m_vm','systemVersion','systemVersion');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('bcc0c8083b9541768253c0c117ef854f','m_vm','hostEnv','hostEnv');

insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('e8da39510d4e43e09d1ead2540b8b8eb','m_nd','syncId','id');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('26d4cffadf794082b0d5d30266bae099','m_nd','manageIp','manageIp');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('fa911cd780964b20a2f51d3a252d2c35','m_nd','hostName','hostName');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('e873ccf26a73444d83da59bf929f3b55','m_nd','roomId','roomId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('92ef65b2c7af4201b07c2dcb5473ab10','m_nd','categoryId','categoryId');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('b33e5c2558684c25936e3faa81b15c30','m_nd','softwareVersion','softwareVersion');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('f5216d897d73446d881a5b55fc382844','m_nd','manageType','manageType');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('d7521c628b8045daae05b606c7fecb85','m_nd','deviceDesc','deviceDesc');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('96006e09ac454a40bdd3718b656afb03','m_nd','snNo','snNo');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('863fcc1c7dd941f1aed9ff9b0a49a08d','m_nd','finNo','finNo');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('7331ec9310f0401e92ac114ecaf1bc61','m_nd','manufactor','manufactor');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('9efff9712e934780bf180b1576c4d0c4','m_nd','brand','brand');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('6a805bf3ee5641328667d3bccf105f75','m_nd','equipmentType','equipmentType');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('383602ed1845498183d464c367f71392','m_nd','mainStart','mainStartStr');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('93c129075d3440cfba5ffe512bdea027','m_nd','mainEnd','mainEndStr');
insert into cmdb_sync_form(id,module_code,form_code,sync_code) values('0729dfba4fba465aa27849524f4a3132','m_nd','remark','remark');