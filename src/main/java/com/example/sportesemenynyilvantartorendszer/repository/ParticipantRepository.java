package com.example.sportesemenynyilvantartorendszer.repository;

import com.example.sportesemenynyilvantartorendszer.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository
        extends JpaRepository<Participant, Long> {
}
