
CREATE TABLE resource_project (
	project_uuid varchar(36) NOT NULL,
	resource_uuid varchar(36) NOT NULL,
	belongs_to_project varchar(20),
	primary key (project_uuid,resource_uuid)
);
ALTER TABLE resources_resource ADD knamespace_uuid varchar(36);














