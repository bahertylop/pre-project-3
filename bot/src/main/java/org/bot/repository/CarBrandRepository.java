package org.bot.repository;

import org.bot.model.CarBrand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

    @Query("from CarBrand carBrand order by similarity(carBrand.name, ?1) desc")
    List<CarBrand> getSimilarCarBrands(String brandName, Pageable pageable);
}
