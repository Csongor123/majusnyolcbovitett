package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.EventService;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WebControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private ParticipantService participantService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpServletRequest request;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private WebController webController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        when(eventService.findAll()).thenReturn(List.of(new Event()));
        String view = webController.index(model);
        assertEquals("index", view);
        verify(model).addAttribute(eq("events"), any());
        verify(model).addAttribute(eq("title"), eq("Összes sportesemény"));
    }

    @Test
    void testShowAddPage() {
        when(eventService.findAll()).thenReturn(List.of(new Event()));
        String view = webController.showAddPage(model);
        assertEquals("add", view);
        verify(model).addAttribute(eq("participant"), any());
        verify(model).addAttribute(eq("events"), any());
    }

    @Test
    void testAddParticipantWithValidationErrors() {
        Participant p = new Participant();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = webController.addParticipant(
                p, bindingResult, null, null, null, redirectAttributes);

        assertEquals("redirect:/add", view);
        verify(redirectAttributes).addFlashAttribute(eq("org.springframework.validation.BindingResult.participant"), eq(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq("participant"), eq(p));
    }

    @Test
    void testAddParticipantWithInvalidAge() {
        Participant p = new Participant();
        p.setAge(120); // kívül esik az engedélyezett tartományon
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = webController.addParticipant(
                p, bindingResult, null, null, null, redirectAttributes);

        assertEquals("redirect:/add", view);
        verify(redirectAttributes).addAttribute("error", "invalidAge");
    }

    @Test
    void testAddParticipantWithoutEventOrNewEvent() {
        Participant p = new Participant();
        p.setAge(25);
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = webController.addParticipant(
                p, bindingResult, null, null, null, redirectAttributes);

        assertEquals("redirect:/add", view);
        verify(redirectAttributes).addAttribute("error", "noEvent");
    }

    @Test
    void testAddParticipantWithNewEvent() {
        Participant p = new Participant();
        p.setAge(25);
        p.setActivityDate(LocalDate.now());
        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.save(any(Event.class))).thenReturn(new Event());

        String view = webController.addParticipant(
                p, bindingResult, "Futás", "Teszt", "Teszt Helyszín", redirectAttributes);

        assertEquals("redirect:/", view);
        verify(eventService).save(any());
        verify(participantService).save(any());
    }

    @Test
    void testAddParticipantWithExistingEvent() {
        Participant p = new Participant();
        p.setAge(30);
        Event e = new Event();
        e.setId(1L);
        e.setDate(LocalDate.now());
        p.setEvent(e);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.findById(1L)).thenReturn(Optional.of(e));

        String view = webController.addParticipant(
                p, bindingResult, null, null, null, redirectAttributes);

        assertEquals("redirect:/", view);
        verify(participantService).save(any());
    }

    @Test
    void testDeleteParticipant() {
        when(request.getHeader("Referer")).thenReturn("/teszt");
        String result = webController.deleteParticipant(5L, request);
        assertEquals("redirect:/teszt", result);
        verify(participantService).deleteById(5L);
    }

    @Test
    void testDeleteParticipantWithoutReferer() {
        when(request.getHeader("Referer")).thenReturn(null);
        String view = webController.deleteParticipant(1L, request);
        assertEquals("redirect:/", view);
        verify(participantService).deleteById(1L);
    }

    @Test
    void testToggleFavorite() {
        Event event = new Event();
        event.setFavorite(false);
        when(eventService.findById(1L)).thenReturn(Optional.of(event));
        when(request.getHeader("Referer")).thenReturn("/fav");

        String result = webController.toggleFavorite(1L, request);
        assertEquals("redirect:/fav", result);
        verify(eventService).save(any());
    }

    @Test
    void testUnfollowFavorite() {
        Event event = new Event();
        event.setFavorite(true);
        when(eventService.findById(2L)).thenReturn(Optional.of(event));
        when(request.getHeader("Referer")).thenReturn("/fav");

        String result = webController.unfollowFavorite(2L, request);
        assertEquals("redirect:/fav", result);
        verify(eventService).save(any());
    }

    @Test
    void testFavoritesPage() {
        Event favoriteEvent = new Event();
        favoriteEvent.setFavorite(true);
        when(eventService.findAll()).thenReturn(List.of(favoriteEvent));

        String result = webController.favoritesPage(model);
        assertEquals("event-list", result);
        verify(model).addAttribute(eq("events"), any());
        verify(model).addAttribute(eq("title"), eq("Kedvenc események"));
    }

    @Test
    void testCompletedEvents() {
        Event pastEvent = new Event();
        pastEvent.setDate(LocalDate.now().minusDays(1));
        when(eventService.findAll()).thenReturn(List.of(pastEvent));

        String result = webController.completedEvents(model);
        assertEquals("event-list", result);
        verify(model).addAttribute(eq("events"), any());
        verify(model).addAttribute(eq("title"), eq("Teljesített események"));
    }

    @Test
    void testTennisPage() {
        Event e = new Event();
        e.setDate(LocalDate.now());
        when(eventService.findByCategory("Tenisz")).thenReturn(List.of(e));

        String view = webController.tennisPage(model);
        assertEquals("event-list", view);
        verify(model).addAttribute(eq("events"), any());
        verify(model).addAttribute(eq("title"), eq("Tenisz események"));
    }
}
