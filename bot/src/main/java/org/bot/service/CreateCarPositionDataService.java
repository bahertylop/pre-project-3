package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.bot.model.CreateCarPositionData;
import org.bot.repository.CreateCarPositionDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCarPositionDataService {

    private final CreateCarPositionDataRepository carPositionDataRepository;

    @Transactional
    public void deleteAllCarPositionDataByChatId(Long chatId) {
        carPositionDataRepository.deleteAllByChatId(chatId);
    }

    public CreateCarPositionData getCarPosition(Long chatId) {
        return carPositionDataRepository.findByChatId(chatId);
    }

    public void createCarPositionData(Long chatId, String carBrand) {
        CreateCarPositionData data = CreateCarPositionData.builder()
                .chatId(chatId)
                .brandName(carBrand)
                .build();
        carPositionDataRepository.save(data);
    }

    public void setModelToCarCreatePositionData(Long chatId, String carModel) {
        CreateCarPositionData data = getCarPosition(chatId);
        data.setModelName(carModel);
        carPositionDataRepository.save(data);
    }
}
