package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CarPositionDto;
import org.example.dto.request.CreateCarPositionRequest;
import org.example.dto.response.CarPositionResponse;
import org.example.exception.CarBrandNotFoundException;
import org.example.exception.CarModelNotFoundException;
import org.example.exception.CarPositionAccessDeniedException;
import org.example.exception.CarPositionNotFoundException;
import org.example.model.CarBrand;
import org.example.model.CarModel;
import org.example.model.CarPosition;
import org.example.model.User;
import org.example.repository.CarBrandRepository;
import org.example.repository.CarModelRepository;
import org.example.repository.CarPositionRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarPositionService {

    private final CarPositionRepository carPositionRepository;

    private final CarBrandRepository carBrandRepository;

    private final CarModelRepository carModelRepository;

    private final PositionParsingService positionParsingService;

    public List<CarPositionDto> getUserCars(UserDetails userDetails) {
        User user = (User) userDetails;
        return CarPositionDto.from(carPositionRepository.findByUser(user));
    }

    public CarPositionResponse getUserCarPosition(UserDetails userDetails, Long carPositionId) {
        Optional<CarPosition> carPositionOp = carPositionRepository.findById(carPositionId);
        if (carPositionOp.isEmpty()) {
            log.info("CarPosition with id: {} not found", carPositionId);
            throw new CarPositionNotFoundException("Позиция с id: " + carPositionId + " не найдена");
        }

        User user = (User) userDetails;
        if (!Objects.equals(user.getId(), carPositionOp.get().getUser().getId())) {
            log.info("User with id: {} try get not his position carPositionId: {}", user.getId(), carPositionId);
            throw new CarPositionAccessDeniedException("Пользователь обращается не к своей позиции");
        }

        return CarPositionResponse.from(carPositionOp.get());
    }

    public void createCarPosition(UserDetails userDetails, CreateCarPositionRequest request) {
        User user = (User) userDetails;

        Optional<CarBrand> carBrandOp = carBrandRepository.findCarBrandByName(request.getBrand());
        if (carBrandOp.isEmpty()) {
            log.info("CarBrand with name: {} not found", request.getBrand());
            throw new CarBrandNotFoundException("Марка с name: " + request.getBrand() + " не найдена");
        }
        Optional<CarModel> carModelOp = carModelRepository.findCarModelByBrandAndName(carBrandOp.get(), request.getModel());
        if (carModelOp.isEmpty()) {
            log.info("CarModel with name: {} not found", request.getModel());
            throw new CarModelNotFoundException("Модель с name: " + request.getModel() + " не найдена");
        }

        CarPosition carPosition = CarPosition.builder()
                .brand(carBrandOp.get())
                .model(carModelOp.get())
                .yearFrom(request.getYearFrom())
                .yearBefore(request.getYearBefore())
                .mileageFrom(request.getMileageFrom())
                .mileageBefore(request.getMileageBefore())
                .user(user)
                .build();
        carPositionRepository.save(carPosition);

        positionParsingService.parseCarPosition(carPosition);
    }

    public void deleteCarPosition(UserDetails userDetails, Long carPositionId) {
        Optional<CarPosition> carPositionOp = carPositionRepository.findById(carPositionId);

        if (carPositionOp.isEmpty()) {
            log.info("CarPosition with id: {} not found", carPositionId);
            throw new CarPositionNotFoundException("Позиция с id: " + carPositionId + " не найдена");
        }
        User user = (User) userDetails;
        if (!Objects.equals(user.getId(), carPositionOp.get().getUser().getId())) {
            log.info("User with id: {} try get not his position carPositionId: {}", user.getId(), carPositionId);
            throw new CarPositionAccessDeniedException("Пользователь обращается не к своей позиции");
        }

        carPositionRepository.delete(carPositionOp.get());
    }
}
