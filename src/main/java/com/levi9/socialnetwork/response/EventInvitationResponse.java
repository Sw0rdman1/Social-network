package com.levi9.socialnetwork.response;

import com.levi9.socialnetwork.entity.EventInvitationStatusEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventInvitationResponse {
    private Long id;
    private String invitee;
    private EventResponse event;
    private EventInvitationStatusEntity status;
}