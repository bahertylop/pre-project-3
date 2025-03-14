package org.example.parsing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.CarPosition;
import org.example.repository.CarPositionRepository;
import org.example.repository.PositionPriceRepository;
import org.example.service.PositionParsingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PositionParser {

    private final CarPositionRepository carPositionRepository;

    private final PositionParsingService positionParsingService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void parsePositionPrice() {
        List<CarPosition> carPositions = carPositionRepository.findAll();

        log.info("add prices for {} positions for date {}", carPositions.size(), LocalDate.now());
        for (CarPosition position : carPositions) {
            log.info("parse position with id {}", position.getId());
            positionParsingService.parseCarPosition(position);
        }

        log.info("{} positions parsed", carPositions.size());
    }
}
