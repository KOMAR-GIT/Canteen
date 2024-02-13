package com.moika22.canteen.repository;

import com.moika22.canteen.model.Staff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends CrudRepository<Staff, Integer> {

    @Override
    Optional<Staff> findById(Integer integer);
}
