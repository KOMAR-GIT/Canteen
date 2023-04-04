package main.repositories;

import main.model.RegisterEvents;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RegisterEventsRepository extends CrudRepository<RegisterEvents, Integer> {

    Optional<RegisterEvents> findByLastTimestamp(Date lastTimestamp);
}
