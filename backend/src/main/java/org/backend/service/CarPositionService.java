package org.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.dto.CarPositionDto;
import org.backend.dto.response.CarPositionResponse;
import org.backend.exception.*;
import org.backend.model.CarBrand;
import org.backend.model.CarModel;
import org.backend.model.CarPosition;
import org.backend.model.User;
import org.backend.repository.CarBrandRepository;
import org.backend.repository.CarModelRepository;
import org.backend.repository.CarPositionRepository;
import org.dto.request.CreateCarPositionRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    private final UserService userService;

    private final PositionParsingService positionParsingService;

    public List<CarPositionDto> getUserCars(UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("user gets car positions userId: {}", user.getId());
        return CarPositionDto.from(carPositionRepository.findByUserId(user.getId()));
    }

    public List<CarPositionDto> getUserCars(Long userid) {
        if (userService.getUserById(userid).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id: " + userid + " не найден");
        }

        log.info("get user car positions by userId: {}", userid);
        return CarPositionDto.from(carPositionRepository.findByUserId(userid));
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

    public CarPositionResponse getCarPosition(Long carPositionId) {
        Optional<CarPosition> carPositionOp = carPositionRepository.findById(carPositionId);
        if (carPositionOp.isEmpty()) {
            log.info("CarPosition with id: {} not found", carPositionId);
            throw new CarPositionNotFoundException("Позиция с id: " + carPositionId + " не найдена");
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

        Optional<CarPosition> carPositionExists = findDuplicateCarPosition(carPosition);
        if (carPositionExists.isPresent()) {
            log.info("CarPosition already exists id: {}", carPositionExists.get().getId());
            throw new CarPositionAlreadyExistsException("Позиция уже добавлена, id: " + carPositionExists.get().getId());
        }

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

    private Optional<CarPosition> findDuplicateCarPosition(CarPosition carPosition) {
        return carPositionRepository.findByUser(carPosition.getUser())
                .stream().filter(carPosition::equals).findFirst();
    }
}
