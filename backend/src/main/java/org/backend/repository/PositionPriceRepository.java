package org.backend.repository;

import org.backend.model.PositionPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionPriceRepository extends JpaRepository<PositionPrice, Long> {
}
