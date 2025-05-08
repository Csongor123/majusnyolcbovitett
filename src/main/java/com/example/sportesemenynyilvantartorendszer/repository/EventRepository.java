package com.example.sportesemenynyilvantartorendszer.repository;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e "
            + "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> findByKeyword(@Param("keyword") String keyword);

    List<Event> findByCategory(String category);
}
