package main.repositories;

import main.model.RegisterEvents;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterEventsRepository extends CrudRepository<RegisterEvents, Integer> {

    @Query(value = "SELECT * from REG_EVENTS where AREAS_ID = :areasId and LAST_TIMESTAMP between :start and :finish", nativeQuery = true)
    List<Optional<RegisterEvents>> getEvents(@Param("areasId") Integer areasId,@Param("start") String firstTimestamp, @Param("finish") String lastTimestamp);
}
