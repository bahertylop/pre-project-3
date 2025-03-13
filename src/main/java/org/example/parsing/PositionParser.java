package org.example.parsing;

import lombok.RequiredArgsConstructor;
import org.example.model.CarPosition;
import org.example.repository.PositionPriceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionParser {

    private final PositionPriceRepository positionPriceRepository;

    public void parsePositionPrice(CarPosition carPosition) {

    }
}
