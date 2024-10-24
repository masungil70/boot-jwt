CREATE TABLE `tbl_member` (
	`uid` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`pwd` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`uuid` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`roles` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`uid`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

