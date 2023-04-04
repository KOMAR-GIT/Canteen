package main.repositories;

import main.model.Staff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends CrudRepository<Staff, Integer> {

    @Override
    Optional<Staff> findById(Integer integer);
}
