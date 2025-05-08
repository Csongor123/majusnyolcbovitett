package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> findAll();

    List<Event> findByKeyword(String keyword);

    List<Event> findByCategory(String category);

    Optional<Event> findById(Long id);

    Event save(Event event);

    void deleteById(Long id);
}
