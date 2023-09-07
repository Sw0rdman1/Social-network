package com.levi9.socialnetwork.util.notifications;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String emailTo;
    private String subject;
    private String text;
}
