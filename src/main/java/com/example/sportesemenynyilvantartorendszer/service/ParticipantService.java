package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Participant;
import java.util.List;
import java.util.Optional;

public interface ParticipantService {

    List<Participant> findAll();

    Optional<Participant> findById(Long id);

    Participant save(Participant participant);

    void deleteById(Long id);

    void updateParticipant(Long id, Participant updatedParticipant);

}
