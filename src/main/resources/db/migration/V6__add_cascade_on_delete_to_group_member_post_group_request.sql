ALTER TABLE `group_member`
DROP FOREIGN KEY `fk_groupmember_group`;

ALTER TABLE `group_member`
ADD CONSTRAINT `fk_groupmember_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `group_request`
DROP FOREIGN KEY `fk_grouprequest_group`;

ALTER TABLE `group_request`
ADD  CONSTRAINT `fk_grouprequest_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `post`
DROP FOREIGN KEY `fk_post_group`;

ALTER TABLE `post`
ADD CONSTRAINT `fk_post_group` FOREIGN KEY (`group_id`) REFERENCES `social_group` (`id`) ON UPDATE CASCADE ON DELETE CASCADE;