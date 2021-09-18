CREATE TABLE `wechaty_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` varchar(45) NOT NULL,
  `from_id` varchar(45) DEFAULT NULL,
  `to_id` varchar(45) NOT NULL,
  `origin_message_id` varchar(45) DEFAULT NULL,
  `content` varchar(3000) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `payload` json DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
