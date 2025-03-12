package org.example.repository;

import org.example.model.PositionPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionPriceRepository extends JpaRepository<PositionPrice, Long> {
}
