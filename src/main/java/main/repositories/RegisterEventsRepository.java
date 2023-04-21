package main.repositories;

import main.model.RegisterEvents;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterEventsRepository extends CrudRepository<RegisterEvents, Integer> {

    @Query(value = "SELECT ID_REG, STAFF_ID, LAST_TIMESTAMP FROM REG_EVENTS where AREAS_ID = 101310", nativeQuery = true)
    List<RegisterEvents> getEvents();
}
