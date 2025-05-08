package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipantServiceImplTest {

    private ParticipantRepository participantRepository;
    private ParticipantServiceImpl participantService;

    @BeforeEach
    void setUp() {
        participantRepository = mock(ParticipantRepository.class);
        participantService = new ParticipantServiceImpl(participantRepository);
    }

    @Test
    void testFindAll() {
        when(participantRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(participantService.findAll().isEmpty());
        verify(participantRepository).findAll();
    }

    @Test
    void testFindByIdWhenPresent() {
        Participant participant = new Participant();
        participant.setId(1L);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        Optional<Participant> result = participantService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindByIdWhenNotPresent() {
        when(participantRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Participant> result = participantService.findById(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSave() {
        Participant participant = new Participant();
        participant.setName("Teszt");
        when(participantRepository.save(participant)).thenReturn(participant);
        Participant saved = participantService.save(participant);
        assertEquals("Teszt", saved.getName());
        verify(participantRepository).save(participant);
    }
}
