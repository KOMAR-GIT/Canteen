package com.moika22.canteen.repository;

import com.moika22.canteen.model.RegisterEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterEventsRepository extends CrudRepository<RegisterEvent, Integer> {

    @Query(value = "SELECT ID_REG, STAFF_ID, LAST_TIMESTAMP FROM REG_EVENTS where AREAS_ID = 101310 and LAST_TIMESTAMP > :time", nativeQuery = true)
    List<RegisterEvent> getEvents(@Param("time") String timestamp);

    @Query(value = "DELETE FROM REG_EVENTS WHERE LAST_TIMESTAMP < :time", nativeQuery = true)
    void clearOldEvents(@Param("time") String timestamp);
}
