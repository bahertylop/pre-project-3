package org.example.repository;

import org.example.model.CarBrand;
import org.example.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    Optional<CarModel> findCarModelByBrandAndName(CarBrand carBrand, String name);

    List<CarModel> findCarModelByBrandId(Long brandId);
}
