package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sportesemenynyilvantartorendszer.exception.NoSuchEntityException;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantRestController {

    private final ParticipantService participantService;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService
                .findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant>
    getParticipantById(@PathVariable Long id) {
        Participant participant = participantService.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(
                        "Nincs ilyen résztvevő ID: " + id));
        return ResponseEntity.ok(participant);
    }

    @PostMapping
    public Participant createParticipant(@RequestBody Participant participant) {
        return participantService.save(participant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(
            @PathVariable Long id,
            @RequestBody Participant updatedParticipant) {
        participantService.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(
                        "Nincs ilyen résztvevő ID: " + id));
        updatedParticipant.setId(id);
        return ResponseEntity.ok(participantService.save(updatedParticipant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(
                        "Nincs ilyen résztvevő ID: " + id));
        participantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
