package org.backend.repository;

import org.backend.model.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

    Optional<CarBrand> findCarBrandByName(String name);
}
