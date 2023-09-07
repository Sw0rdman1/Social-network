ALTER TABLE `social-network`.`friend_request`
	ADD COLUMN `request_counter` INT UNSIGNED DEFAULT 0 NULL AFTER `receiver_id`;
ALTER TABLE `social-network`.`friend_request`
    CHANGE `status` `status` ENUM('REJECTED','PENDING');
