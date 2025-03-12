package org.example.repository;

import org.example.model.CarPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarPositionRepository extends JpaRepository<CarPosition, Long> {
}
