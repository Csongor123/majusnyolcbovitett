package com.example.sportesemenynyilvantartorendszer.controller.rest;



import com.example.sportesemenynyilvantartorendszer.controller.EventRestController;
import com.example.sportesemenynyilvantartorendszer.exception.NoSuchEntityException;
import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventRestControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventRestController eventRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        when(eventService.findAll()).thenReturn(List.of(new Event(), new Event()));

        List<Event> result = eventRestController.getAllEvents();

        assertEquals(2, result.size());
        verify(eventService).findAll();
    }

    @Test
    void testGetEventById_found() {
        Event event = new Event();
        event.setId(1L);
        when(eventService.findById(1L)).thenReturn(Optional.of(event));

        ResponseEntity<Event> response = eventRestController.getEventById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(event, response.getBody());
    }

    @Test
    void testGetEventById_notFound() {
        when(eventService.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, () -> eventRestController.getEventById(99L));
    }

    @Test
    void testGetEventsByCategory() {
        when(eventService.findByCategory("Futás")).thenReturn(List.of(new Event()));

        List<Event> result = eventRestController.getEventsByCategory("Futás");

        assertEquals(1, result.size());
        verify(eventService).findByCategory("Futás");
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        when(eventService.save(event)).thenReturn(event);

        Event result = eventRestController.createEvent(event);

        assertEquals(event, result);
    }

    @Test
    void testUpdateEvent_found() {
        Event updated = new Event();
        updated.setName("Updated");
        when(eventService.findById(1L)).thenReturn(Optional.of(new Event()));
        when(eventService.save(updated)).thenReturn(updated);

        ResponseEntity<Event> response = eventRestController.updateEvent(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
    }

    @Test
    void testUpdateEvent_notFound() {
        Event updated = new Event();
        when(eventService.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, () -> eventRestController.updateEvent(999L, updated));
    }

    @Test
    void testDeleteEvent_found() {
        when(eventService.findById(1L)).thenReturn(Optional.of(new Event()));

        ResponseEntity<Void> response = eventRestController.deleteEvent(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(eventService).deleteById(1L);
    }

    @Test
    void testDeleteEvent_notFound() {
        when(eventService.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, () -> eventRestController.deleteEvent(100L));
    }
}