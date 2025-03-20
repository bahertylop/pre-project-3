package org.backend.repository;

import org.backend.model.CarPosition;
import org.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarPositionRepository extends JpaRepository<CarPosition, Long> {

    List<CarPosition> findByUser(User user);

    List<CarPosition> findByUserId(Long userId);
}
