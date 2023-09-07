package com.levi9.socialnetwork.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveOrDeclineGroupRequest {
    Long requestId;
    boolean approve;
}
