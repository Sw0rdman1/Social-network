package com.levi9.socialnetwork.controllers;

import org.springframework.http.HttpStatus;
import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.mapper.EventMapper;
import com.levi9.socialnetwork.response.EventResponse;
import com.levi9.socialnetwork.service.event.EventService;
import lombok.AllArgsConstructor;
import com.levi9.socialnetwork.request.EventRequest;
import com.levi9.socialnetwork.response.EventResponse;
import com.levi9.socialnetwork.service.event.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.service.event.EventService;
import org.springframework.web.bind.annotation.DeleteMapping;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping(value = "/{groupId}/event")
    public ResponseEntity<EventEntity> createEvent(@PathVariable Long groupId,
                                                     @RequestBody @Valid EventRequest eventRequest) {
        EventEntity eventResponse = eventService.createEvent(groupId, eventRequest.getLocation(),
                eventRequest.getDateTime());
        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.remove(eventId);
        return ResponseEntity.ok().build();
    }

}
