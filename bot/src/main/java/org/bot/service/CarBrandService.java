package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.bot.mapper.CarBrandMapper;
import org.bot.repository.CarBrandRepository;
import org.dto.CarBrandDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;

    private final CarBrandMapper carBrandMapper;

    public List<CarBrandDto> getCarBrands() {
        return carBrandMapper.toDto(carBrandRepository.findAll());
    }

    public Optional<CarBrandDto> getCarBrandById(Long carBrandId) {
        return carBrandRepository.findById(carBrandId).map(carBrandMapper::toDto);
    }

    public List<CarBrandDto> getSimilarCarBrands(String brandName) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

        return getCarBrands().stream()
                .sorted(Comparator.comparingInt(
                                brand -> levenshteinDistance.apply(brandName.toLowerCase(), brand.getName().toLowerCase())
                        )
                )
                .limit(6).collect(Collectors.toList());
    }

    public List<CarBrandDto> getSimilarCarBrandsUsePG(String brandName) {
        Pageable pageable = Pageable.ofSize(6);
        return carBrandMapper.toDto(carBrandRepository.getSimilarCarBrands(brandName, pageable));
    }
}
