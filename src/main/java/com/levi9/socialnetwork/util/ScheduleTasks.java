package com.levi9.socialnetwork.util;

import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.repository.EventRepository;
import com.levi9.socialnetwork.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ScheduleTasks {

    private final PostRepository postRepository;
    private final EventRepository eventRepository;

    @Scheduled(fixedRate = 60000)
    public void scheduleEventDeletion() {
        try {
            for(EventEntity event : eventRepository.findAll())
            {
                if(event.getDateTime().isAfter(LocalDateTime.now()))
                    eventRepository.delete(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 600000)
    public void scheduleSoftDelete() {
        try {
            List<PostEntity> expiredPosts = postRepository.findExpiredPosts();
            for (PostEntity expiredPost : expiredPosts) {
                expiredPost.setDeleted(true);
                postRepository.save(expiredPost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}