ALTER TABLE `social-network`.`event`
    ADD COLUMN `group_id` int unsigned NOT NULL AFTER `creator_id`,
    ADD KEY `fk_event_group` (`group_id`),
    ADD CONSTRAINT `fk_event_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON
UPDATE CASCADE;

CREATE TABLE `event_invitation`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `invitee_id` int unsigned NOT NULL,
    `event_id`   int unsigned NOT NULL,
    `status`     ENUM('NOT_COMING', 'COMING'),
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQUE_ids` (`invitee_id`,`event_id`),
    KEY          `fk_event_invitation_invitee` (`invitee_id`),
    KEY          `fk_event_invitation_event` (`event_id`),
    CONSTRAINT `fk_event_invitation_invitee` FOREIGN KEY (`invitee_id`) REFERENCES `group_member` (`id`) ON UPDATE CASCADE,
    CONSTRAINT `fk_event_invitation_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;