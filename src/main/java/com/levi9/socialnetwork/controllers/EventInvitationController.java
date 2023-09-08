package com.levi9.socialnetwork.controllers;

import com.levi9.socialnetwork.response.EventInvitationResponse;
import com.levi9.socialnetwork.service.eventinvitation.EventInvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("events")
@AllArgsConstructor
public class EventInvitationController {

    private final EventInvitationService eventInvitationService;

    @PutMapping(value = "/{invitationId}")
    public ResponseEntity<EventInvitationResponse> acceptEventInvitation(@PathVariable Long invitationId) {
        EventInvitationResponse eventInvitationResponse = eventInvitationService.acceptEventInvitation(invitationId);
        return new ResponseEntity<>(eventInvitationResponse, HttpStatus.OK);
    }
}