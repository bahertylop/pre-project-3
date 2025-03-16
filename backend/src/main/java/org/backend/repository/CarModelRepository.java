package org.backend.repository;

import org.backend.model.CarBrand;
import org.backend.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    Optional<CarModel> findCarModelByBrandAndName(CarBrand carBrand, String name);

    List<CarModel> findCarModelByBrandId(Long brandId);
}
