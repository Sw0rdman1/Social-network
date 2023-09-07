CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(25) NOT NULL,
  `active` boolean DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_mail` (`email`),
  UNIQUE KEY `UNIQUE_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `social_group` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `closed` boolean DEFAULT 0,
  `admin_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_group_admin` (`admin_id`),
  CONSTRAINT `fk_group_admin` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `post` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `date_created` datetime DEFAULT NULL,
  `text` text DEFAULT NULL,
  `closed` boolean DEFAULT 0,
  `deleted` boolean DEFAULT 0,
  `creator_id` varchar(255) NOT NULL,
  `group_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_post_creator` (`creator_id`),
  KEY `fk_post_group` (`group_id`),
  CONSTRAINT `fk_post_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_post_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `comment` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `text` text DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `post_id` int unsigned NOT NULL,
  `creator_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_commented_post` (`post_id`),
  CONSTRAINT `fk_commented_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `comment_reply` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `text` text DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `comment_id` int unsigned NOT NULL,
  `creator_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_replied_comment` (`comment_id`),
  CONSTRAINT `fk_replied_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_comment_reply_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `friend_request` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `status` ENUM('ACCEPTED', 'REJECTED', 'PENDING'),
  `sender_id` varchar(255) NOT NULL,
  `receiver_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ids` (`sender_id`,`receiver_id`),
  KEY `fk_friendrequest_receiver` (`receiver_id`),
  CONSTRAINT `fk_friendrequest_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_friendrequest_sender` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `friendship` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `date_connected` datetime DEFAULT NULL,
  `user1_id` varchar(255) NOT NULL,
  `user2_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ids` (`user1_id`,`user2_id`),
  KEY `fk_friendship_user2` (`user2_id`),
  KEY `fk_friendship_user1` (`user1_id`),
  CONSTRAINT `fk_friendship_user1` FOREIGN KEY (`user1_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_friendship_user2` FOREIGN KEY (`user2_id`) REFERENCES `user` (`id`),
  CONSTRAINT `self_friendship` CHECK (`user1_id` <> `user2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `group_member` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `date_joined` datetime DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  `group_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ids` (`user_id`,`group_id`),
  KEY `fk_groupmember_group` (`group_id`),
  CONSTRAINT `fk_groupmember_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_groupmember_member` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `group_request` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `status` ENUM('ACCEPTED', 'REJECTED', 'PENDING'),
  `sender_id` varchar(255) NOT NULL,
  `group_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ids` (`sender_id`,`group_id`),
  KEY `fk_grouprequest_group` (`group_id`),
  CONSTRAINT `fk_grouprequest_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_grouprequest_sender` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `event` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `location` varchar(255) NOT NULL,
  `creator_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_event_creator` (`creator_id`),
  CONSTRAINT `fk_event_creator` FOREIGN KEY (`creator_id`) REFERENCES `group_member` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `post_hidden_from` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `post_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ids` (`user_id`,`post_id`),
  KEY `fk_posthiddenfrom_post` (`post_id`),
  CONSTRAINT `fk_posthiddenfrom_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_posthiddenfrom_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
