package com.example.sportesemenynyilvantartorendszer.controller.rest;

import com.example.sportesemenynyilvantartorendszer.controller.ParticipantRestController;
import com.example.sportesemenynyilvantartorendszer.exception.NoSuchEntityException;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ParticipantRestControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantRestController participantRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllParticipants() {
        when(participantService.findAll()).thenReturn(List.of(new Participant()));
        List<Participant> result = participantRestController.getAllParticipants();
        assertEquals(1, result.size());
        verify(participantService).findAll();
    }

    @Test
    void testGetParticipantById_found() {
        Participant participant = new Participant();
        when(participantService.findById(1L)).thenReturn(Optional.of(participant));
        ResponseEntity<Participant> response = participantRestController.getParticipantById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participant, response.getBody());
    }

    @Test
    void testGetParticipantById_notFound() {
        when(participantService.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> participantRestController.getParticipantById(99L));
    }

    @Test
    void testCreateParticipant() {
        Participant participant = new Participant();
        when(participantService.save(participant)).thenReturn(participant);
        Participant result = participantRestController.createParticipant(participant);
        assertEquals(participant, result);
    }

    @Test
    void testDeleteParticipant() {
        Participant p = new Participant();
        when(participantService.findById(1L)).thenReturn(Optional.of(p));
        ResponseEntity<Void> response = participantRestController.deleteParticipant(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(participantService).deleteById(1L);
    }
    @Test
    void testUpdateParticipant() {
        Participant updated = new Participant();
        updated.setId(1L);
        when(participantService.findById(1L)).thenReturn(Optional.of(new Participant()));
        when(participantService.save(updated)).thenReturn(updated);

        ResponseEntity<Participant> response =
                participantRestController.updateParticipant(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(participantService).save(updated);
    }
}
