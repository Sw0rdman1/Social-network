package com.levi9.socialnetwork.response;

import com.levi9.socialnetwork.entity.RequestStatusEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GroupRequestResponse {

    private Long id;
    private String sender;
    private String receiver;
    private String group;
    private RequestStatusEntity status;
    private boolean adminSentRequest;
}
