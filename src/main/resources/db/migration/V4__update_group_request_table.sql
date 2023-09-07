ALTER TABLE `social-network`.`group_request`
    RENAME COLUMN sender_id to user_id;
ALTER TABLE `social-network`.`group_request`
    ADD COLUMN `request_counter` INT UNSIGNED DEFAULT 0 NULL AFTER `group_id`;
ALTER TABLE `social-network`.`group_request`
    CHANGE `status` `status` ENUM('REJECTED','PENDING');
ALTER TABLE `social-network`.`group_request`
    ADD COLUMN `admin_sent_request` boolean DEFAULT 0 AFTER `request_counter`;