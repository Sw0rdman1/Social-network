ALTER TABLE `comment`
    DROP FOREIGN KEY `fk_commented_post`;

ALTER TABLE `comment`
    ADD CONSTRAINT `fk_commented_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `post` (`id`)
            ON UPDATE CASCADE
            ON DELETE CASCADE;