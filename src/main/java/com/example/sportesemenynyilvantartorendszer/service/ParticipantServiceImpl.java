package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.exception.NoSuchEntityException;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    @Override
    public Optional<Participant> findById(Long id) {
        return participantRepository.findById(id);
    }

    @Override
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    public void deleteById(Long id) {
        participantRepository.deleteById(id);
    }

    @Override
    public void updateParticipant(Long id, Participant updatedParticipant) {
        Participant existing = participantRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(
                        "Résztvevő nem található ID alapján: " + id));

        existing.setName(updatedParticipant.getName());
        existing.setAge(updatedParticipant.getAge());
        existing.setEmail(updatedParticipant.getEmail());
        existing.setActivityDate(updatedParticipant.getActivityDate());
        existing.setEvent(updatedParticipant.getEvent());

        participantRepository.save(existing);
    }
}
