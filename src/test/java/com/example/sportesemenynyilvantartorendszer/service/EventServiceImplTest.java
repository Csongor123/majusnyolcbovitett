package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    private EventRepository eventRepository;
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        eventService = new EventServiceImpl(eventRepository);
    }

    @Test
    void testFindAll() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.findAll();

        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> found = eventService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Event> result = eventService.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByKeyword() {
        Event event = new Event();
        event.setName("Úszás");
        when(eventRepository.findByKeyword("úszás")).thenReturn(List.of(event));

        List<Event> result = eventService.findByKeyword("úszás");

        assertEquals(1, result.size());
        assertEquals("Úszás", result.get(0).getName());
    }

    @Test
    void testFindByCategory() {
        Event event = new Event();
        event.setCategory("Futás");
        when(eventRepository.findByCategory("Futás")).thenReturn(List.of(event));

        List<Event> result = eventService.findByCategory("Futás");

        assertEquals(1, result.size());
        assertEquals("Futás", result.get(0).getCategory());
    }

    @Test
    void testSave() {
        Event event = new Event();
        event.setName("Kosárlabda");
        when(eventRepository.save(event)).thenReturn(event);

        Event saved = eventService.save(event);

        assertEquals("Kosárlabda", saved.getName());
    }

    @Test
    void testDeleteById() {
        Long id = 5L;

        eventService.deleteById(id);

        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void testCapitalize_nullInput() {
        String result = invokeCapitalize(null);
        assertNull(result);
    }

    @Test
    void testCapitalize_emptyString() {
        String result = invokeCapitalize("");
        assertEquals("", result);
    }

    @Test
    void testCapitalize_normal() {
        String result = invokeCapitalize("futás");
        assertEquals("Futás", result);
    }


    private String invokeCapitalize(String keyword) {
        try {
            var method = EventServiceImpl.class.getDeclaredMethod("capitalize", String.class);
            method.setAccessible(true);
            return (String) method.invoke(eventService, keyword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
